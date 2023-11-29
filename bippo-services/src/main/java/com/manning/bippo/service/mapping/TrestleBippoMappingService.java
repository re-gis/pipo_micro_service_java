package com.manning.bippo.service.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.TrestleFrontPhotoRepository;
import com.manning.bippo.dao.TrestleHARRepository;
import com.manning.bippo.dao.TrestlePhotoRepository;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.TrestleFrontPhoto;
import com.manning.bippo.dao.pojo.TrestleHAR;
import com.manning.bippo.dao.pojo.TrestlePhoto;
import com.manning.bippo.service.address.standardize.AddressStandardizationService;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.mapping.pojo.TrestleApiData;
import com.manning.bippo.service.profiling.ProfilingMetricsService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * A class supporting features for data interoperability with Trestle API data.
 */
@Service
public class TrestleBippoMappingService {

    public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean verboseFieldMappingFailures = true;

    static {
        DATETIME.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Autowired
    TrestleFrontPhotoRepository trestleFrontPhotoRepository;

    @Autowired
    TrestlePhotoRepository trestlePhotoRepository;

    @Autowired
    TrestleHARRepository trestleHARRepository;

    @Autowired
    AddressStandardizationService addressStandardizationService;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Autowired
    @Qualifier("onboard")
    AddressVerificationService onboardAddressVerificationService;

    @Autowired
    ProfilingMetricsService profilingMetricsService;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    /**
     * @param trestleProperties Properties to process, properties where indexing fails will be removed from this List
     */
    public void importTrestleProperties(List<? extends TrestleProperty> trestleProperties)
    {
        int externalCount = 0, manualCount = 0, wrongTypeCount = 0, badDataCount = 0, skippedCount = 0;

        for (final Iterator<? extends TrestleProperty> it = trestleProperties.iterator(); it.hasNext(); ) {
            final TrestleProperty trestleProperty = it.next();
            final String propertyType = trestleProperty.getPropertyType();

            if (propertyType == null || (!"Residential".equals(propertyType) && !"ResidentialLease".equals(propertyType))) {
                wrongTypeCount++;
                it.remove();
                continue;
            } else if (!trestleProperty.hasValidAddress()) {
                badDataCount++;
                it.remove();
                continue;
            } else if (this.addressSemanticizationService.findTopByTrestleProperty(trestleProperty) != null) {
                // Don't remove, as indexing did not fail; we just already have this property indexed
                continue;
            }

            try {
                final AddressVerificationResponse standardizedAvr = this.addressStandardizationService.standardizeAbstractProperty(trestleProperty);
                final AddressSemantics as;

                if (standardizedAvr != null) {
                    manualCount++;
                    as = this.addressSemanticizationService.forceSemanticize(standardizedAvr, null);
                } else if (RetsBippoMappingServiceImpl.allowSmartyAddressVerification) {
                    externalCount++;
                    as = this.addressSemanticizationService.semanticize(trestleProperty.getAddress(), null);
                } else if (RetsBippoMappingServiceImpl.allowAttomAddressVerification) {
                    try {
                        as = this.addressSemanticizationService.forceSemanticize(this.onboardAddressVerificationService.query(trestleProperty), null);
                        externalCount++;
                    } catch (Exception e) {
                        skippedCount++;
                        it.remove();
                        continue;
                    }
                } else {
                    skippedCount++;
                    it.remove();
                    continue;
                }

                as.setTrestleSystem(trestleProperty.getOriginatingSystemName());
                as.setTrestleId(trestleProperty.getId());
                this.addressSemanticsRepository.save(as);
            } catch (Exception e) {
                LogUtil.error("Couldn't semanticize address: {}", trestleProperty.getAddress(), e);
            }
        }

        LogUtil.info("importTrestleProperties finished; skipped {} ({} type + {} bad + {} skip), indexed {} ({} man + {} ext)",
                wrongTypeCount + badDataCount + skippedCount, wrongTypeCount, badDataCount, skippedCount,
                manualCount + externalCount, manualCount, externalCount);

        if (RetsBippoMappingServiceImpl.allowSmartyAddressVerification) {
            this.profilingMetricsService.incrementCounter("ss_TrestleBippoMappingService_importTrestleProperties", externalCount);
        }
    }

    public void updateAllADataHAR(TrestleApiData data, List<? super TrestleHAR> append) {
        if (!data.validate()) {
            throw new IllegalArgumentException("Unable to validate TrestleApiData");
        }

        final List<TrestleHAR> pending = new ArrayList<>();

        for (int i = 0; i < data.value.length; i++) {
            final TrestleHAR d;

            try {
                d = this.updateDataHAR(data, i);
            } catch (Exception e) {
                LogUtil.error("Error while trying to save Trestle (HAR) response: {}", LogUtil.expand(e));
                continue;
            }

            pending.add(d);
        }

        if (append != null) {
            append.addAll(pending);
        }

        // Call for addresses to be indexed, where possible
        this.importTrestleProperties(pending);
    }

    public TrestleHAR updateDataHAR(TrestleApiData data, int i) {
        if (!data.validate()) {
            throw new IllegalArgumentException("Unable to validate TrestleApiData");
        }

        boolean newImport = true;
        String oldStatus = null;
        float oldPrice = 0;
        TrestleHAR row = this.trestleHARRepository.findByListingKeyNumeric(data.keys[i]);

        if (row == null) {
            // Creating a new row
            row = new TrestleHAR();
        } else {
            // Updating an existing row
            newImport = false;
            oldStatus = row.getStatus();
            oldPrice = row.getListPrice().floatValue();
        }

        final JsonObject json = data.value[i];
        this.rawMapData(json, row);
        row = this.trestleHARRepository.save(row);

        AddressSemantics as = addressSemanticizationService.findTopByTrestleProperty(row);
        this.rabbitMQSenderService.queueTrestleUpdateHook(row, newImport, as == null ? -1L : as.getId(), oldStatus, oldPrice, false);
        // Queue supplementary tasks (like photos) for new imports
        this.preProcessPhotos(json, row);
        // Should we queue lat/long update (if new)?

        return row;
    }

    public void rawMapData(JsonObject map, TrestleProperty opaque) {
        for (final Method method : opaque.getClass().getMethods()) {
            if (method.getName().startsWith("set") && !method.getName().equals("setId")) {
                final String fieldName = WordUtils.capitalize(method.getName().substring(3));
                final JsonElement val = map.get(fieldName);

                try {
                    if (val == null || val.isJsonNull()) {
                        method.invoke(opaque, (Object) null);
                        continue;
                    } else if (!val.isJsonPrimitive()) {
                        LogUtil.warn("Unrecognized field of non-primitive type: {}", fieldName);
                        continue;
                    }

                    /*
                     * Supported field types:
                     * Boolean, Date, Double, Integer, Long, String
                     * We've already accounted for null values at this point
                     */

                    final Class<?> param = method.getParameterTypes()[0];

                    if (param.equals(String.class)) {
                        method.invoke(opaque, val.getAsString());
                    } else if (param.equals(Double.class)) {
                        method.invoke(opaque, val.getAsDouble());
                    } else if (param.equals(Integer.class)) {
                        method.invoke(opaque, val.getAsInt());
                    } else if (param.equals(Long.class)) {
                        method.invoke(opaque, val.getAsLong());
                    } else if (param.equals(Boolean.class)) {
                        method.invoke(opaque, val.getAsBoolean());
                    } else if (param.equals(Date.class)) {
                        Date date;

                        try {
                            date = DATETIME.parse(val.getAsString());
                        } catch (Exception e) {
                            date = DATE.parse(val.getAsString());
                        }

                        method.invoke(opaque, date);
                    }
                } catch (Exception e) {
                    if (TrestleBippoMappingService.verboseFieldMappingFailures) {
                        LogUtil.warn("Error while mapping field: {}", fieldName);
                    }
                }
            }
        }
    }

    public void preProcessPhotos(JsonObject map, TrestleProperty prop) {
        final JsonElement media = map.get("Media");

        if (media != null && media.isJsonArray()) {
            final JsonArray photos = media.getAsJsonArray();
            final int key = prop.getListingKeyNumeric();

            if (photos.size() > 1) {
                for (int i = 1; i < photos.size(); i++) {
                    final String trestlePhotoUrl;

                    try {
                        trestlePhotoUrl = photos.get(i).getAsJsonObject().get("MediaURL").getAsString();
                    } catch (Exception e) {
                        if (TrestleBippoMappingService.verboseFieldMappingFailures) {
                            LogUtil.warn("Error mapping photo " + i + " for ListingKey " + key);
                        }

                        continue;
                    }

                    final TrestlePhoto p = new TrestlePhoto();
                    p.setListingKey(key);
                    p.setTrestleUrl(trestlePhotoUrl);
                    this.trestlePhotoRepository.save(p);
                }
            }

            if (photos.size() > 0) {
                try {
                    final String trestlePhotoUrl = photos.get(0).getAsJsonObject().get("MediaURL").getAsString();
                    TrestleFrontPhoto fp = this.trestleFrontPhotoRepository.findFirstByListingKey(key);

                    if (fp == null) {
                        fp = new TrestleFrontPhoto();
                        fp.setListingKey(key);
                    }

                    fp.setTrestleUrl(trestlePhotoUrl);
                    this.trestleFrontPhotoRepository.save(fp);
                } catch (Exception e) {
                    if (TrestleBippoMappingService.verboseFieldMappingFailures) {
                        LogUtil.warn("Error mapping front photo for ListingKey " + key);
                    }
                }
            }
        }
    }
}
