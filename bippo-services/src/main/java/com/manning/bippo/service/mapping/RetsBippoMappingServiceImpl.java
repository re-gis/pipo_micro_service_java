package com.manning.bippo.service.mapping;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.NtreisPhotoRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.PropertyCompsFilterRepository;
import com.manning.bippo.dao.pojo.*;
import com.manning.bippo.service.address.standardize.AddressStandardizationService;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.profiling.ProfilingMetricsService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import com.manning.bippo.service.rets.RetsService;
import com.manning.bippo.service.rets.pojo.CompsResponse;
import com.manning.bippo.service.rets.pojo.RetsResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.StopWatch;
import org.realtors.rets.client.SingleObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class RetsBippoMappingServiceImpl implements RetsBippoMappingService
{
    public static final int SUCCESSFULLY_UPLOADED = 0;
    public static final int ALREADY_EXISTED = 1;
    public static final int NULL_OR_SKIPPED_IDENTIFIER = 2;
    public static final int ERROR_UPLOADING = 127;
    public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean allowSmartyAddressVerification = false;
    public static boolean allowAttomAddressVerification = false;
    public static boolean skipNonFrontPhotos = false;
    public static boolean verboseFieldMappingFailure = false;

    @Autowired
    RetsService retsService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    @Autowired
    ProfilingMetricsService profilingMetricsService;

    @Autowired
    AddressStandardizationService addressStandardizationService;

    @Autowired
    @Qualifier("onboard")
    AddressVerificationService onboardAddressVerificationService;

    @Autowired(required = false)
    AmazonS3 amazonS3;

    @Autowired
    NtreisPhotoRepository ntreisPhotoRepository;

    /**
     * This method converts the NTREIS Response Object to NTREIS POJO as required by our database.
     */
    @Async
    public List<NtreisProperty> mapRetsResponseToNtreisProperty(List<RetsResponse> retsResponses)
    {
        List<NtreisProperty> ntreisProperties = new ArrayList<>();

        retsResponses.parallelStream().forEach(ntreisResponse -> {
            try {
                ntreisProperties.add(mapRetsResponseToNtreisProperty(ntreisResponse));
            } catch (Exception e) {
                LogUtil.info("Error while trying to save Ntreis response: {}", LogUtil.expand(e));
            }
        });

        return ntreisProperties;
    }

    @Override
    @Async
    public NtreisProperty mapRetsResponseToNtreisProperty(RetsResponse retsResponse)
    {
        return mapRetsResponseToNtreisProperty(retsResponse, true);
    }

    @Override
    public NtreisProperty mapRetsResponseToNtreisProperty(RetsResponse retsResponse, boolean reImport)
    {
        NtreisProperty ntreisProperty = ntreisPropertyRepository.findByMLSNumber(Integer.parseInt(retsResponse.MLSNumber));
        final String oldStatus;
        final float oldPrice;
        final boolean newImport;

        if (ntreisProperty == null) {
            ntreisProperty = new NtreisProperty();
            ntreisProperty.setCreated(new Date());
            ntreisProperty.setUpdated(new Date());
            newImport = true;
            oldStatus = null;
            oldPrice = 0;
        } else {
            if (ntreisProperty.getCreated() == null) {
                ntreisProperty.setCreated(new Date());
            }

            ntreisProperty.setUpdated(new Date());
            newImport = false;
            oldStatus = ntreisProperty.getStatus();
            oldPrice = ntreisProperty.getListPrice();
        }

        mapResponseToProperty(retsResponse, ntreisProperty);
        ntreisProperty = ntreisPropertyRepository.save(ntreisProperty);

        {
            final AddressSemantics as = this.addressSemanticsRepository.findTopByNtreisProperty(ntreisProperty);
            this.rabbitMQSenderService.queueMlsUpdateHook(ntreisProperty, newImport, as == null ? -1L : as.getId().longValue(), oldStatus, oldPrice);
        }

        if (newImport) {
            rabbitMQSenderService.queueDownloadFrontPhotos(Collections.singletonList(ntreisProperty.getId()));
            rabbitMQSenderService.queueDownloadPhotos(Collections.singletonList(ntreisProperty.getId()));
        }

        rabbitMQSenderService.queueDownloadLatLong(Collections.singletonList(ntreisProperty.getId()));
        LogUtil.info("{} Matrix ID {}", newImport ? "Inserted new" : "Updated existing", retsResponse.Matrix_Unique_ID);
        return ntreisProperty;
    }

    void mapResponseToProperty(RetsResponse retsResponse, NtreisProperty ntreisProperty)
    {
//        List<Field> ntreisResponseFields = Arrays.asList(retsResponse.getClass().getDeclaredFields());

        for (Method method : ntreisProperty.getClass().getMethods()) {
            if (method.getName().startsWith("set") && !method.getName().equals("setId")) {
                final String fieldName = WordUtils.capitalize(method.getName().substring(3));

                try {
                    final Field ntreisResponseField = retsResponse.getClass().getField(fieldName);

                    if (method.getParameterTypes()[0].equals(String.class)) {
                        method.invoke(ntreisProperty, ntreisResponseField.get(retsResponse));
                        continue;
                    }

                    final String stringValue;

                    {
                        final Object fieldValue = ntreisResponseField.get(retsResponse);

                        if (fieldValue != null) {
                            stringValue = fieldValue.toString();

                            if (stringValue.isEmpty()) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }

                    if (method.getParameterTypes()[0].equals(Integer.class)) {
                        Integer value = Integer.valueOf(stringValue);
                        method.invoke(ntreisProperty, value);
                    }

                    if (method.getParameterTypes()[0].equals(Float.class)) {
                        Float value = Float.valueOf(stringValue);
                        method.invoke(ntreisProperty, value);
                    }

                    if (method.getParameterTypes()[0].equals(Date.class)) {
                        Date value;

                        try {
                            value = DATETIME.parse(stringValue);
                        } catch (Exception e) {
                            value = DATE.parse(stringValue);
                        }

                        method.invoke(ntreisProperty, value);
                    }
                } catch (Exception e) {
                    if (verboseFieldMappingFailure) {
                        LogUtil.warn("Error while mapping field: {}", e.getMessage());
                    }
                }
            }
        }
    }

    public List<NtreisProperty> importNtreisProperties(List<NtreisProperty> ntreisProperties)
    {
        List<NtreisProperty> persistentNtreisProperties = new ArrayList<>();
        int externalCount = 0, manualCount = 0, wrongTypeCount = 0, badDataCount = 0, skippedCount = 0;

        for (NtreisProperty ntreisProperty : ntreisProperties) {
            final String propertyType = ntreisProperty.getPropertyType();

            if (propertyType == null || (!"Residential".equals(propertyType) && !"Residential Lease".equals(propertyType))) {
                wrongTypeCount++;
                continue;
            } else if (!ntreisProperty.hasValidAddress()) {
                badDataCount++;
                continue;
            } else if (this.addressSemanticizationService.findTopByNtreisProperty(ntreisProperty) != null) {
                // Don't force semanticize when we already have this address indexed
                continue;
            }

            try {
                final AddressVerificationResponse standardizedAvr = this.addressStandardizationService.standardizeAbstractProperty(ntreisProperty);
                final AddressSemantics as;

                if (standardizedAvr != null) {
                    manualCount++;
                    as = this.addressSemanticizationService.forceSemanticize(standardizedAvr, null);
                } else if (RetsBippoMappingServiceImpl.allowSmartyAddressVerification) {
                    externalCount++;
                    as = this.addressSemanticizationService.semanticize(ntreisProperty.getAddress(), null);
                } else if (RetsBippoMappingServiceImpl.allowAttomAddressVerification) {
                    try {
                        as = this.addressSemanticizationService.forceSemanticize(this.onboardAddressVerificationService.query(ntreisProperty), null);
                        externalCount++;
                    } catch (Exception e) {
                        skippedCount++;
                        continue;
                    }
                } else {
                    skippedCount++;
                    continue;
                }

                persistentNtreisProperties.add(ntreisProperty);
                as.setNtreisProperty(ntreisProperty);
                this.addressSemanticsRepository.save(as);
            } catch (IOException | IllegalStateException e) {
                LogUtil.error("Couldn't semanticize address: {}", ntreisProperty.getAddress(), e);
            }
        }

        LogUtil.info("importNtreisProperties finished; skipped {} ({} type + {} bad + {} skip), indexed {} ({} man + {} ext)",
                wrongTypeCount + badDataCount + skippedCount, wrongTypeCount, badDataCount, skippedCount,
                manualCount + externalCount, manualCount, externalCount);

        if (RetsBippoMappingServiceImpl.allowSmartyAddressVerification) {
            this.profilingMetricsService.incrementCounter("ss_RetsBippoMappingServiceImpl_importNtreisProperties", externalCount);
        }

        return persistentNtreisProperties;
    }

    public List<NtreisProperty> importNtreisProperties(String firstLine, String lastLine, List<NtreisProperty> ntreisProperties)
    {
        List<NtreisProperty> persistentNtreisProperties = new ArrayList<>();

        for (NtreisProperty ntreisProperty : ntreisProperties) {
            addToAddressSemantic(firstLine, lastLine, ntreisProperty);

            if (ntreisProperty.getId() != null) {
                persistentNtreisProperties.add(ntreisProperty);
            } else {
                // TODO: this method usually is called after  mapRetsResponseToNtreisProperty, which is already saving the property.
            }
        }

        return persistentNtreisProperties;
    }

    private void addToAddressSemantic(String firstLine, String lastLine, NtreisProperty persistentNtreisProperty)
    {
        AddressSemantics addressSemantics = addressSemanticsRepository.findTopByFirstLineAndLastLine(firstLine, lastLine, new Sort(Sort.Direction.DESC, "id"));

        if (addressSemantics != null) {
            LogUtil.info("Adding NTREIS Property for property : {}, {}", firstLine, lastLine);
            addressSemantics.setNtreisProperty(persistentNtreisProperty);
            addressSemanticsRepository.save(addressSemantics);
        }
    }


    @Override
    public List<NtreisProperty> importMLSRecord(String firstLine, String lastLine, String primaryNumber,
            String streetName, String streetSuffix, String cityName, Integer zipCode)
    {
        return importNtreisProperties(firstLine, lastLine, mapRetsResponseToNtreisProperty(
                retsService.findMLSRecord(primaryNumber, streetName, streetSuffix, cityName, zipCode)));
    }

    @Override
    public List<NtreisProperty> importSoldMLSRecords(Date startTime, Date endTime)
    {
        return importNtreisProperties(mapRetsResponseToNtreisProperty(retsService.getSoldMLSRecords(startTime, endTime)));
    }

    @Override
    public List<NtreisProperty> importUpdatedMLSRecords(Date startTime, Date endTime)
    {
        return importNtreisProperties(mapRetsResponseToNtreisProperty(retsService.getUpdatedMLSRecords(startTime, endTime)));
    }

    @Override
    public List<NtreisProperty> importUpdatedMLSRecords(String matrixUniqueId, boolean semanticize)
    {
        final List<NtreisProperty> imported = mapRetsResponseToNtreisProperty(retsService.getUpdatedMLSRecords(matrixUniqueId));
        return semanticize ? importNtreisProperties(imported) : imported;
    }

    @Override
    public List<NtreisProperty> importUpdatedMLSRecords(int mlsNumber)
    {
        return importNtreisProperties(mapRetsResponseToNtreisProperty(retsService.getUpdatedMLSNumbers(mlsNumber)));
    }

    @Override
    @Transactional
    public void getAllPhotos(List<Long> id)
    {
        final List<NtreisProperty> needsOthers = ntreisPropertyRepository.findAll(id);
        final List<NtreisProperty> needsFront = new ArrayList<>();

        for (final Iterator<NtreisProperty> it = needsOthers.iterator(); it.hasNext(); ) {
            final NtreisProperty prop = it.next();
            final List<NtreisPhoto> photos = prop.getNtreisPhotos();

            if (photos.stream().noneMatch(p -> p.getType().equalsIgnoreCase("Photo"))) {
                needsFront.add(prop);
            }

            if (photos.stream().anyMatch(p -> !p.getType().equalsIgnoreCase("Photo"))) {
                it.remove();
            }
        }

        final List<NtreisPhoto> ntreisPhotos = new ArrayList<>();

        if (skipNonFrontPhotos) {
            if (!needsFront.isEmpty()) {
                ntreisPhotos.addAll(getFrontPhotosByType(needsFront, "Photo"));
            }

            // TODO Register a task to come back to these IDs later for HighRes and LargePhoto?
        } else {
            if (!needsFront.isEmpty()) {
                ntreisPhotos.addAll(getFrontPhotosByType(needsFront, "Photo"));
            }

            if (!needsOthers.isEmpty()) {
                ntreisPhotos.addAll(getPhotosByType(needsOthers, "HighRes"));
                ntreisPhotos.addAll(getPhotosByType(needsOthers, "LargePhoto"));
            }
        }

        ntreisPhotos.forEach(ntreisPhotoRepository::save);
    }

    @Override
    @Transactional
    public void markPhotosDoNotCheck(List<Long> id)
    {
        final List<NtreisProperty> shouldHavePhotos = ntreisPropertyRepository.findAll(id);
        final List<NtreisPhoto> ntreisPhotos = new ArrayList<>();

        // Assert that all of these properties have photos; if they do not, mark them with a DoNotCheck mock/"poison" photo
        for (NtreisProperty prop : shouldHavePhotos) {
            final List<NtreisPhoto> photos = prop.getNtreisPhotos();

            if (photos.stream().noneMatch(p -> p.getType().equalsIgnoreCase("Photo")) || photos.stream().noneMatch(p -> !p.getType().equalsIgnoreCase("Photo"))) {
                // Property is missing front photos, non-front photos, or both
                ntreisPhotos.add(new NtreisPhoto(prop, "", "DoNotCheck"));
            }
        }

        if (ntreisPhotos.isEmpty()) {
            return;
        }

        LogUtil.debug("Marking {}x DoNotCheck for absent photos after processing.", ntreisPhotos.size());
        ntreisPhotos.forEach(ntreisPhotoRepository::save);
    }

    @Override
    @Transactional
    public void getFrontPhotos(List<Long> propertyIds)
    {
        final List<NtreisProperty> needsFront = ntreisPropertyRepository.findAll(propertyIds);

        for (final Iterator<NtreisProperty> it = needsFront.iterator(); it.hasNext(); ) {
            final NtreisProperty prop = it.next();

            if (prop.getNtreisPhotos().stream().anyMatch(p -> p.getType().equalsIgnoreCase("Photo"))) {
                // We already have a front photo for this property
                it.remove();
            }
        }

        if (!needsFront.isEmpty()) {
            getFrontPhotosByType(needsFront, "Photo").forEach(ntreisPhotoRepository::save);
        }
    }

    List<NtreisPhoto> getFrontPhotosByType(List<NtreisProperty> ntreisProperties, String type)
    {
        List<NtreisPhoto> ntreisPhotos = new ArrayList<>();
        StopWatch timer = new StopWatch();
        timer.start();
        final int nProp;
        int nPhotos = 0, nDuplicates = 0, nErrors = 0, nSkip = 0;

        try {
            Map<String, NtreisProperty> ntreisPropertyMap = ntreisProperties.stream()
                    .collect(Collectors.toMap(ntreisProperty -> ntreisProperty.getMatrix_Unique_ID().toString(), ntreisProperty -> ntreisProperty));
            nProp = ntreisPropertyMap.size();

            for (final Iterator<SingleObjectResponse> singleObjectResponseIter = retsService.getFrontPhoto(ntreisPropertyMap.keySet(), type).iterator(); singleObjectResponseIter.hasNext(); ) {
                switch (downloadPhotoToS3(type, ntreisPhotos, ntreisPropertyMap, singleObjectResponseIter.next())) {
                    case SUCCESSFULLY_UPLOADED:
                        nPhotos++;
                        break;
                    case ALREADY_EXISTED:
                        nDuplicates++;
                        break;
                    case NULL_OR_SKIPPED_IDENTIFIER:
                        nSkip++;
                        break;
                    default:
                        nErrors++;
                        break;
                }
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            return ntreisPhotos;
        }

        timer.stop();
        LogUtil.debug("Downloaded Front {} for {}x in {}ms. {} ERR, {} SKIP, {} DUP, {} NEW.", type, nProp, timer.getTime(), nErrors, nSkip, nDuplicates, nPhotos);
        return ntreisPhotos;
    }

    List<NtreisPhoto> getPhotosByType(List<NtreisProperty> ntreisProperties, String type)
    {
        List<NtreisPhoto> ntreisPhotos = new ArrayList<>();
        StopWatch timer = new StopWatch();
        timer.start();
        final int nProp;
        int nPhotos = 0, nDuplicates = 0, nErrors = 0, nSkip = 0;

        try {
            Map<String, NtreisProperty> ntreisPropertyMap = ntreisProperties.stream()
                    .collect(Collectors.toMap(ntreisProperty -> ntreisProperty.getMatrix_Unique_ID().toString(), ntreisProperty -> ntreisProperty));
            nProp = ntreisPropertyMap.size();

            for (final Iterator<SingleObjectResponse> singleObjectResponseIter = retsService.getAllPhotos(ntreisPropertyMap.keySet(), type).iterator(); singleObjectResponseIter.hasNext(); ) {
                switch (downloadPhotoToS3(type, ntreisPhotos, ntreisPropertyMap, singleObjectResponseIter.next())) {
                    case SUCCESSFULLY_UPLOADED:
                        nPhotos++;
                        break;
                    case ALREADY_EXISTED:
                        nDuplicates++;
                        break;
                    case NULL_OR_SKIPPED_IDENTIFIER:
                        nSkip++;
                        break;
                    default:
                        nErrors++;
                        break;
                }
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            return ntreisPhotos;
        }

        timer.stop();
        LogUtil.debug("Downloaded {} for {}x in {}ms. {} ERR, {} SKIP, {} DUP, {} NEW.", type, nProp, timer.getTime(), nErrors, nSkip, nDuplicates, nPhotos);
        return ntreisPhotos;
    }

    private int downloadPhotoToS3(String type, List<NtreisPhoto> ntreisPhotos, Map<String, NtreisProperty> ntreisPropertyMap, SingleObjectResponse singleObjectResponse)
    {
        try {
            if (singleObjectResponse.getContentID() != null && (singleObjectResponse.getObjectID() != null && !singleObjectResponse.getObjectID().equalsIgnoreCase("*"))) {
                NtreisProperty ntreisProperty = ntreisPropertyMap.get(singleObjectResponse.getContentID());
                String fileName = buildFileName(ntreisProperty.getCountyOrParish().toLowerCase(), singleObjectResponse.getContentID(),
                        type, singleObjectResponse.getContentID(), singleObjectResponse.getObjectID());
                String s3Url = "https://s3.amazonaws.com/" + s3BucketName + "/" + fileName;

                try {
                    amazonS3.getObjectMetadata(s3BucketName, fileName);

                    if (ntreisPhotoRepository.findFirstByS3Url(s3Url) == null) {
                        ntreisPhotos.add(new NtreisPhoto(ntreisProperty, s3Url, type));
                    }

                    return ALREADY_EXISTED;
                } catch (AmazonS3Exception s3e) {
                    if (s3e.getStatusCode() == 404) {
                        File scratchFile = File.createTempFile(ntreisProperty.getMatrix_Unique_ID() + "-" + type, ".jpeg");

                        try {
                            FileUtils.copyInputStreamToFile(singleObjectResponse.getInputStream(), scratchFile);
                            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, scratchFile);
                            amazonS3.putObject(putObjectRequest);
                        } finally {
                            if (scratchFile.exists()) {
                                scratchFile.delete();
                            }
                        }

                        ntreisPhotos.add(new NtreisPhoto(ntreisProperty, s3Url, type));
                        return SUCCESSFULLY_UPLOADED;
                    } else {
                        throw s3e;
                    }
                } finally {
                    singleObjectResponse.getInputStream().close();
                    singleObjectResponse = null;
                }
            } else {
                if (singleObjectResponse.getContentID() != null || singleObjectResponse.getObjectID() != null) {
                    LogUtil.debug("Bad IDs: " + singleObjectResponse.getContentID() + ", " + singleObjectResponse.getObjectID());
                }

                return NULL_OR_SKIPPED_IDENTIFIER;
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            return ERROR_UPLOADING;
        }
    }

    private static String s3BucketName = "badphotos";

    public static String buildS3URL(String county, String matrixUniqueId, String type, String contentID, String objectID)
    {
        return "https://s3.amazonaws.com/" + s3BucketName + "/" + buildFileName(county, matrixUniqueId, type, contentID, objectID);
    }

    public static String buildFileName(String county, String matrixUniqueId, String type, String contentID, String objectID)
    {
        return "TX/" + county + "/" + matrixUniqueId + "-"
                + type + "-" + contentID + "-" + objectID + ".jpeg";
    }

    @Override
    public void importByFilter(PropertySearchFilter retsSearch)
    {
        Page<RetsResponse> resultPage = importByPage(retsSearch, 1);
        LogUtil.debug("Importing by filter, {} pages total.", resultPage.getTotalPages());

        for (int page = 2; page < resultPage.getTotalPages(); page++) {
            resultPage = importByPage(retsSearch, page);
        }
    }

    private Page<RetsResponse> importByPage(PropertySearchFilter retsSearch, int page)
    {
        final StopWatch timer = new StopWatch();
        timer.start();

        final Page<RetsResponse> bySearchFilter = retsService.findBySearchFilter(retsSearch, page, 1000);
        timer.stop();
        LogUtil.debug("Queried page {} of filter in {}ms.", page, timer.getTime());

        try {
            bySearchFilter.getContent().forEach(retsResponse -> mapRetsResponseToNtreisProperty(retsResponse, false));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        return bySearchFilter;
    }

    @Autowired
    PropertyCompsFilterRepository propertyCompsFilterRepository;

    @Override
    @Transactional
    public void importComps(NtreisProperty subject, List<CompsFilterCalculated> compsFilter, String type)
    {
        LogUtil.debug("Running Rets Service to import comps for filter");
        CompsResponse comps = retsService.findComps(subject.getLatitude().doubleValue(), subject.getLongitude().doubleValue(), compsFilter);

        try {
            if (comps.getRetsResponses() != null && comps.getRetsResponses().size() > 0) {
                PropertyCompsFilter propertyCompsFilter = propertyCompsFilterRepository.findTopBySubjectOrderByUpdated(subject);

                if (propertyCompsFilter == null) {
                    propertyCompsFilter = new PropertyCompsFilter();
                    propertyCompsFilter.setSubject(subject);
                    setFilter(type, comps, propertyCompsFilter);
                    propertyCompsFilter.setPropertyComps(new HashSet<>());
                } else {
                    setFilter(type, comps, propertyCompsFilter);
                }

                PropertyCompsFilter finalPropertyCompsFilter = propertyCompsFilter;
                comps.getRetsResponses().forEach(retsResponse -> {
                    NtreisProperty insertedProerty = mapRetsResponseToNtreisProperty(retsResponse, false);
                    PropertyComp propertyComp = new PropertyComp(finalPropertyCompsFilter, insertedProerty, type);
                    finalPropertyCompsFilter.getPropertyComps().add(propertyComp);
                });
                propertyCompsFilterRepository.save(propertyCompsFilter);
            }
        } catch (Exception e)
        {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void importComps(BasicPropertyDetails subject, List<CompsFilterCalculated> compsFilter, String compsSearchType)
    {
        LogUtil.debug("Running Rets Service for: " + compsFilter.toString());
        CompsResponse comps = retsService.findComps(subject.getLatitude().doubleValue(), subject.getLongitude().doubleValue(), compsFilter);
        AddressSemantics addressSemantics = addressSemanticsRepository.findOne(subject.getAddressSemanticsId());

        try {
            if (comps.getRetsResponses() != null && comps.getRetsResponses().size() > 0) {
                PropertyCompsFilter propertyCompsFilter = propertyCompsFilterRepository.findTopBySubjectAddressSemanticsOrderByUpdated(addressSemantics);

                if (propertyCompsFilter == null) {
                    propertyCompsFilter = new PropertyCompsFilter();
                    propertyCompsFilter.setSubjectAddressSemantics(addressSemantics);
                    setFilter(compsSearchType, comps, propertyCompsFilter);
                    propertyCompsFilter.setPropertyComps(new HashSet<>());
                } else {
                    setFilter(compsSearchType, comps, propertyCompsFilter);
                }

                PropertyCompsFilter finalPropertyCompsFilter = propertyCompsFilter;
                comps.getRetsResponses().forEach(retsResponse -> {
                    NtreisProperty insertedProperty = mapRetsResponseToNtreisProperty(retsResponse, false);

                    /**
                     * FIX ME: is this needed? It seems we never use property_comps table to get the comps, we use redis to get them.
                     */
                    PropertyComp propertyComp = new PropertyComp(finalPropertyCompsFilter, insertedProperty, compsSearchType);
                    finalPropertyCompsFilter.getPropertyComps().add(propertyComp);
                });
                propertyCompsFilterRepository.save(propertyCompsFilter);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    private void setFilter(String type, CompsResponse comps, PropertyCompsFilter propertyCompsFilter)
    {
        if (type.equals("LEASE")) {
            propertyCompsFilter.setLeaseFilterUsed(comps.getCompsFilterUsed());
        } else if (type.equals("ARV")) {
            propertyCompsFilter.setArvFilterUsed(comps.getCompsFilterUsed());
        } else {
            propertyCompsFilter.setAreaFilterUsed(comps.getCompsFilterUsed());
        }
    }
}
