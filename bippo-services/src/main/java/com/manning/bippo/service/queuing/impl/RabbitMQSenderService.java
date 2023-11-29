package com.manning.bippo.service.queuing.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.*;
import com.manning.bippo.dao.TrestleFrontPhotoRepository;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.NtreisPhoto;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.TrestleFrontPhoto;
import com.manning.bippo.dao.pojo.TrestlePhoto;
import com.manning.bippo.service.property.BasicPropertyBuilderService;
import com.manning.bippo.service.trestle.TrestleDataUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.util.CollectionUtils;

@Service
public class RabbitMQSenderService
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    TrestleFrontPhotoRepository trestleFrontPhotoRepository;

    private RabbitAdmin rabbitAdmin;

    void logQueueSize(String queueName)
    {
        try {
            if (this.rabbitAdmin == null) {
                this.rabbitAdmin = new RabbitAdmin(this.rabbitTemplate.getConnectionFactory());
                this.rabbitAdmin.initialize();
            }

            final Properties qp = this.rabbitAdmin.getQueueProperties(queueName);
            LogUtil.info("[" + queueName + "] now has " + qp.getProperty("QUEUE_MESSAGE_COUNT") + " entries");
        } catch (Exception e) {
            LogUtil.info("Failed to use RabbitAdmin to check on queue.. " + e.getMessage());
            return;
        }
    }

    Map<Object, Object> logQueue(String queueName)
    {
        try {
            if (this.rabbitAdmin == null) {
                this.rabbitAdmin = new RabbitAdmin(this.rabbitTemplate.getConnectionFactory());
                this.rabbitAdmin.initialize();
            }

            return this.rabbitAdmin.getQueueProperties(queueName);
        } catch (Exception e) {
            LogUtil.info("Failed to use RabbitAdmin to check on queue.. " + e.getMessage());
            return null;
        }
    }

    public Map<String, Map<Object, Object>> debugLogAllQueues() {
        final Map<String, Map<Object, Object>> props = new HashMap<>();

        for (String queue : new String[] { "bippo-dag", "bippo-dag-rets", "bippo-dag-arv", "bippo-dag-lat-lang-queue", "bippo-dag-update-mls-records",
            "bippo-dag-rets-comps", "bippo-dag-rets-front-photos", "bippo-dag-rets-photos", "bippo-mls-update-hook", "bippo-dag-trestle" }) {
            props.put(queue, this.logQueue(queue));
        }

        return props;
    }

    public void queueDownloadPhotos(List<Long> ntreisPropertyId)
    {
        try {
            ntreisPropertyId.forEach(id -> {
                this.rabbitTemplate.convertAndSend("bippo-dag-rets-photos", new DownloadMultiplePropertyPhotos(Collections.singletonList(id)));
            });
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueDownloadLatLong(List<Long> ntreisPropertyIds)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-lat-lang-queue", new DownloadMultiplePropertyLatLong(ntreisPropertyIds));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void updateMlsRecordForNumber(int mlsNumber)
    {
        this.rabbitTemplate.convertAndSend("bippo-dag-update-mls-records", new DownloadMultiplePropertyMLS(mlsNumber));
    }

    public void updateMLSRecordsForProperties(List<NtreisProperty> ntreisProperties)
    {
        for (NtreisProperty prop : ntreisProperties) {
            this.rabbitTemplate.convertAndSend("bippo-dag-update-mls-records", new DownloadMultiplePropertyMLS(Integer.toString(prop.getMatrix_Unique_ID()), false));
        }
    }

    public void updateMLSRecordsForProperty(NtreisProperty ntreisProperty)
    {
        this.rabbitTemplate.convertAndSend("bippo-dag-update-mls-records", new DownloadMultiplePropertyMLS(Integer.toString(ntreisProperty.getMatrix_Unique_ID()), false));
    }

    public void updateMLSRecords(List<String> matrixIds)
    {
        for (String id : matrixIds) {
            if (id != null && !id.isEmpty()) {
                this.rabbitTemplate.convertAndSend("bippo-dag-update-mls-records", new DownloadMultiplePropertyMLS(id, false));
            }
        }
    }

    public void queueDownloadOnBoardData(Long ntreisPropertyId)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag", new DownloadOnBoardInformaticsResponse(ntreisPropertyId));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueDownloadMLS(Date startTime, Date endTime)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-rets", new DownloadRetsUpdated(startTime, endTime));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueDownloadTrestle(String originatingSystem, Date startTime, Date endTime)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-trestle", new DownloadTrestleRange(originatingSystem, startTime, endTime));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void updateMlsRecordTrestleForNumber(String region, int mlsNumber)
    {
        this.rabbitTemplate.convertAndSend("bippo-dag-trestle", new DownloadTrestleSingle(region, mlsNumber));
    }

    public void queueDownloadLeaseProperties(List<Long> propertyIds)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-rets", new DownloadLeaseProperties(propertyIds));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueARVComps(List<Long> propertyIds)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-arv", new BuildARVComps(propertyIds));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueDownloadFrontPhotos(List<Long> propertyIds)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-rets-front-photos", new DownloadMultiplePropertyFrontPhotos(propertyIds));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }


    public void queueUpdateRedisCache(List<Long> propertyIds)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag", new UpdateRedisCache(propertyIds));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueUpdateRedisCacheTrestle(String region, List<Long> propertyIds)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag", new UpdateRedisCacheTrestle(region, propertyIds));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueDebugRedisCaches()
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag", new DebugRedisCaches());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueTruncateRedisCache(String region)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag", new TruncateRedisCache(region));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueTruncateRedisCacheByDate(String region, Date dateBefore)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag", new TruncateRedisCacheByDate(region, dateBefore));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queuePropertySearchFilter(PropertySearchFilter propertySearchFilter)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-rets", propertySearchFilter);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @Autowired
    BasicPropertyBuilderService basicPropertyBuilderService;

    public void queueDownloadRETSAreaComps(BasicPropertyDetails subject)
    {
        try {
            this.rabbitTemplate.convertAndSend("bippo-dag-rets-comps", new DownloadAreaComps(subject, true));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueDownloadRETSAreaComps(NtreisProperty subject, boolean forceSemantics)
    {
        BasicPropertyDetails build = basicPropertyBuilderService.build(subject, forceSemantics);
        queueDownloadRETSAreaComps(build);
    }

    public void queueMlsUpdateHook(NtreisProperty prop, boolean newImport, long addressSemanticsId, String oldStatus, float oldPrice, boolean verboseDebug)
    {
        try {
            final boolean statusUpdated = !prop.getStatus().equals(oldStatus), priceUpdated = oldPrice != prop.getListPrice().floatValue();

            if (!newImport && !statusUpdated && !priceUpdated) {
                // Only send an update if at least either the status or price have been updated (a new listing counts)
                LogUtil.info(
                        "Skipped queueMlsUpdateHook newStatus {}, statusUpdated {}, priceUpdated {}",
                        newImport,
                        statusUpdated,
                        priceUpdated);
                return;
            }

            // TODO: Add a common class between bippo-central and hit-spring-web so we do not have to send a Map
            final Map<String, Object> map = new HashMap<>();

            String propertyImageUrl = null;
            if (!CollectionUtils.isEmpty(prop.getNtreisPhotos())) {
                propertyImageUrl = prop.getNtreisPhotos()
                        .stream()
                        .filter(ntreisPhoto -> ntreisPhoto.getType().equalsIgnoreCase("Photo"))
                        .map(NtreisPhoto::getS3Url)
                        .findFirst()
                        .orElse(null);
            }

            // General fields
            map.put("newImport", newImport);
            map.put("mlsNumber", prop.getMLSNumber());
            map.put("formattedPropertyId", String.format("mlsNumber:%d", prop.getMLSNumber()));
            map.put("addressSemanticsId", addressSemanticsId);
            map.put("address", prop.getAddress());
            map.put("statusUpdated", statusUpdated);
            map.put("priceUpdated", priceUpdated);
            map.put("oldStatus", oldStatus);
            map.put("newStatus", prop.getStatus());
            map.put("oldPrice", oldPrice);
            map.put("newPrice", prop.getListPrice());
            map.put("verboseDebug", verboseDebug);

            // Search filter fields
            map.put("bathsFull", prop.getBathsFull());
            map.put("bathsHalf", prop.getBathsHalf());
            map.put("bedsTotal", prop.getBedsTotal());
            map.put("parkingSpacesCarport", prop.getParkingSpacesCarport());
            map.put("parkingSpacesGarage", prop.getParkingSpacesGarage());
            map.put("city", prop.getCity());
            map.put("countyOrParish", prop.getCountyOrParish());
            map.put("cdom", prop.getCDOM());
            map.put("dom", prop.getDOM());
            map.put("associationFee", prop.getAssociationFee());
            map.put("numberOfStories", prop.getNumberOfStories());
            map.put("propertyType", prop.getPropertyType());
            map.put("propertySubType", prop.getPropertySubType());
            map.put("schoolDistrict", prop.getSchoolDistrict());
            map.put("sqFtTotal", prop.getSqFtTotal());
            map.put("postalCode", prop.getPostalCode());
            map.put("addressLine1", prop.getAddressLine1());
            map.put("addressLine2", prop.getAddressLine2());
            map.put("poolAvailable", "1".equals(prop.getPoolYN()));
            map.put("garageAvailable", prop.getParkingSpacesGarage() != null && prop.getParkingSpacesGarage() > 0);
            map.put("propertyImageUrl", propertyImageUrl);
            map.put("numberOfLivingAreas", prop.getNumberOfLivingAreas());
            map.put("numberOfDiningAreas", prop.getNumberOfDiningAreas());
            map.put("publicRemarks", prop.getPublicRemarks());

            this.rabbitTemplate.convertAndSend("bippo-mls-update-hook", map);
            LogUtil.info("MLS Update Hook [{}{}{}] {}/{}", newImport ? "N" : "", priceUpdated ? "P" : "", statusUpdated ? "S" : "", prop.getMLSNumber(), addressSemanticsId);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueTrestleUpdateHook(
            TrestleProperty prop,
            boolean newImport,
            long addressSemanticsId,
            String oldStatus,
            float oldPrice,
            boolean verboseDebug) {
        try {
            // remap status to ntreis status
            oldStatus = TrestleDataUtils.mapTrestleStatusToNtreis(prop.getPropertyType(), oldStatus);
            String newStatus = TrestleDataUtils.mapTrestleStatusToNtreis(prop.getPropertyType(), prop.getStatus());
            final boolean statusUpdated = !newStatus.equals(oldStatus), priceUpdated =
                    oldPrice != prop.getListPrice().floatValue();

            if (!newImport && !statusUpdated && !priceUpdated) {
                LogUtil.info(
                        "Skipped queueTrestleUpdateHook newStatus {}, statusUpdated {}, priceUpdated {}",
                        newImport,
                        statusUpdated,
                        priceUpdated);
                return;
            }

            final Map<String, Object> map = new HashMap<>();

            String propertyImageUrl = null;
            TrestleFrontPhoto trestlePhoto = trestleFrontPhotoRepository.findFirstByListingKey(prop.getListingKeyNumeric());
            if(trestlePhoto != null) {
                propertyImageUrl = trestlePhoto.getTrestleUrl();
            }

            // General fields
            map.put("newImport", newImport);
            map.put("listingKeyNumeric", prop.getListingKeyNumeric());
            map.put("mlsNumber", prop.getListingId());
            map.put("originatingSystemName", prop.getOriginatingSystemName());
            map.put("formattedPropertyId", String.format("tr:%s_%d", prop.getOriginatingSystemName(), prop.getListingKeyNumeric()));
            map.put("addressSemanticsId", addressSemanticsId);
            map.put("address", prop.getAddress());
            map.put("statusUpdated", statusUpdated);
            map.put("priceUpdated", priceUpdated);
            map.put("oldStatus", oldStatus);
            map.put("newStatus", newStatus);
            map.put("oldPrice", oldPrice);
            map.put("newPrice", prop.getListPrice());
            map.put("verboseDebug", verboseDebug);

            // Search filter fields
            map.put("bathsFull", prop.getBathsFull());
            map.put("bathsHalf", prop.getBathsHalf());
            map.put("bedsTotal", prop.getBedsTotal());
            map.put("parkingSpacesCarport", prop.getParkingSpacesCarport());
            map.put("parkingSpacesGarage", prop.getParkingSpacesGarage());
            map.put("city", prop.getCity());
            map.put("countyOrParish", prop.getCountyOrParish());
            map.put("cdom", prop.getCumulativeDaysOnMarket());
            map.put("dom", prop.getDaysOnMarket());
            map.put("associationFee", prop.getAssociationFee());
            map.put("numberOfStories", prop.getNumberOfStories());
            map.put("propertyType", prop.getPropertyType());
            map.put("propertySubType", prop.getPropertySubType());
            String schoolDistrict = StringUtils.isNotBlank(prop.getElementarySchoolDistrict()) ?
                                    prop.getElementarySchoolDistrict() :
                                    (StringUtils.isNotBlank(prop.getMiddleOrJuniorSchoolDistrict()) ?
                                     prop.getMiddleOrJuniorSchoolDistrict() :
                                     prop.getHighSchoolDistrict());
            map.put("schoolDistrict", schoolDistrict);
            map.put("sqFtTotal", prop.getSqFtTotal());
            map.put("postalCode", prop.getPostalCode());
            map.put("addressLine1", prop.getAddressLine1());
            map.put("addressLine2", prop.getAddressLine2());
            map.put("poolAvailable", "Y".equalsIgnoreCase(prop.getPoolYN()));
            map.put("garageAvailable", prop.getGarageYN());
            map.put("propertyImageUrl", propertyImageUrl);
            // these infos not available for trestle
            //map.put("numberOfLivingAreas", prop.getNumberOfLivingAreas());
            //map.put("numberOfDiningAreas", prop.getNumberOfDiningAreas());
            map.put("publicRemarks", prop.getPublicRemarks());

            this.rabbitTemplate.convertAndSend("bippo-mls-update-hook", map);
            LogUtil.info(
                    "MLS Update Hook Trestle  [{}{}{}] {}/{}",
                    newImport ? "N" : "",
                    priceUpdated ? "P" : "",
                    statusUpdated ? "S" : "",
                    prop.getListingKeyNumeric(),
                    addressSemanticsId);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    public void queueMlsUpdateHook(NtreisProperty prop, boolean newImport, long addressSemanticsId, String oldStatus, float oldPrice)
    {
        this.queueMlsUpdateHook(prop, newImport, addressSemanticsId, oldStatus, oldPrice, false);
    }
}
