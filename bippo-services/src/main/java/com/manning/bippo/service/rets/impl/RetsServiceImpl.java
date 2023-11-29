package com.manning.bippo.service.rets.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.MLSCityRepository;
import com.manning.bippo.dao.pojo.City;
import com.manning.bippo.dao.rets.pojo.RetsLookup;
import com.manning.bippo.dao.rets.pojo.RetsLookupType;
import com.manning.bippo.dao.rets.pojo.RetsResource;
import com.manning.bippo.service.rets.RetsService;
import com.manning.bippo.service.rets.pojo.CompsResponse;
import com.manning.bippo.service.rets.pojo.RetsResponse;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetsServiceImpl extends AbstractRetsService implements RetsService {
    @Autowired
    MLSCityRepository mlsCityRepository;

    @Autowired
    RetsSearchFilterBuilderService retsSearchFilterBuilderService;

    @Override
    public Metadata getMetadata() throws RetsException {
        return getSession().getMetadata();
    }

    @Override
    public RetsResource getResourceMetadata(String resourceName) throws RetsException {
        Metadata metadata = getMetadata();

        RetsResource retsPropertyResource = new RetsResource(resourceName);
        MLookup[] mLookups = metadata.getResource(resourceName).getMLookups();
        for (int i = 0; i < mLookups.length; i++) {
            MLookup mLookup = mLookups[i];
            RetsLookup retsLookup = new RetsLookup(mLookup.getLookupName());
            MLookupType[] mLookupTypes = mLookup.getMLookupTypes();
            for (int j = 0; j < mLookupTypes.length; j++) {
                MLookupType mLookupType = mLookupTypes[j];
                RetsLookupType retsLookupType = new RetsLookupType(mLookupType.getShortValue(), mLookupType.getLongValue());
                retsLookup.getLookupTypes().add(retsLookupType);
            }
            retsPropertyResource.getLookups().add(retsLookup);
        }

        return retsPropertyResource;
    }

    public List<RetsResponse> getSoldMLSRecords(Date startTime, Date endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startTimeFormatted = dateFormat.format(startTime) + "+";
        String endTimeFormatted = dateFormat.format(endTime) + "-";

        LogUtil.info("Downloading Sold Properties between {} - {}.", startTimeFormatted, endTimeFormatted);

        String sQuery = "(MatrixModifiedDT=" + startTimeFormatted + ") AND (MatrixModifiedDT=" + endTimeFormatted + ") AND (STATUS=S)";

        return queryRetsFeed(sQuery, -1, -1).getContent();
    }

    public List<RetsResponse> getUpdatedMLSRecords(Date startTime, Date endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));

        String startTimeFormatted = dateFormat.format(startTime) + "+";
        String endTimeFormatted = dateFormat.format(endTime) + "-";
        String sQuery = "(MatrixModifiedDT=" + startTimeFormatted + ") AND (MatrixModifiedDT=" + endTimeFormatted + ")";

        return queryRetsFeed(sQuery, -1, -1).getContent();
    }

    @Override
    public List<RetsResponse> findMLSRecord(String primaryNumber,
                                            String streetName, String streetSuffix,
                                            String cityName, Integer zipCode) {
        City mlsCity = mlsCityRepository.findByValue(cityName);

        String sQuery = "(StreetNumber=" + primaryNumber + ")" +
                " AND (StreetName=" + streetName + ")" +
                " AND (StreetSuffix=" + streetSuffix + ")" +
                " AND (City=" + mlsCity.getRetsShortValue() + ")" +
                " AND (PostalCode=" + zipCode + ")";

        LogUtil.debug("Rets Query: {}" + sQuery);
        List<RetsResponse> retsResponses = queryRetsFeed(sQuery, -1, -1).getContent();
        LogUtil.info("Found {} MLS records for address {}, {} {}, {}", retsResponses.size(), primaryNumber, streetName, streetSuffix, cityName);

        return retsResponses;
    }

    @Override
    public Page<RetsResponse> findBySearchFilter(PropertySearchFilter propertySearchFilter, int page, int size) {
        try {
            String sQuery = retsSearchFilterBuilderService.buildRetsQuery(propertySearchFilter);
            Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, page, size);

            LogUtil.info("Found {} MLS records for DMQL: {}", retsResponses.getTotalElements(), sQuery);
            return retsResponses;
        } catch (RetsException e) {
            LogUtil.error(e.getMessage(), e);
        }

        return new PageImpl<>(Collections.emptyList(), new PageRequest(0, 0), 0);
    }

    @Override
    public Page<RetsResponse> findNewBySearchFilter(PropertySearchFilter propertySearchFilter, Date startTime, Date endTime, int page, int size) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startTimeFormatted = dateFormat.format(startTime) + "+";
        String endTimeFormatted = dateFormat.format(endTime) + "-";

        try {
            String sQuery = retsSearchFilterBuilderService.buildRetsQuery(propertySearchFilter);
            sQuery += " AND (ListingContractDate=" + startTimeFormatted + ") AND (ListingContractDate=" + endTimeFormatted + ") ";
            Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, page, size);

            LogUtil.info("Found {} MLS records for DMQL: {}", retsResponses.getTotalElements(), sQuery);
            return retsResponses;
        } catch (RetsException e) {
            LogUtil.error(e.getMessage(), e);
        }

        return new PageImpl<>(Collections.emptyList(), new PageRequest(0, 0), 0);
    }

    @Override
    public int getNewResultsCountBySearchFilter(PropertySearchFilter propertySearchFilter, Date startTime, Date endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startTimeFormatted = dateFormat.format(startTime) + "+";
        String endTimeFormatted = dateFormat.format(endTime) + "-";

        try {
            String sQuery = retsSearchFilterBuilderService.buildRetsQuery(propertySearchFilter);
            sQuery += " AND (ListingContractDate=" + startTimeFormatted + ") AND (ListingContractDate=" + endTimeFormatted + ") ";
            return getQueryCount(sQuery);

        } catch (RetsException e) {
            LogUtil.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int getTotalResultsCountBySearchFilter(PropertySearchFilter propertySearchFilter) {
        try {
            String sQuery = retsSearchFilterBuilderService.buildRetsQuery(propertySearchFilter);
            return getQueryCount(sQuery);
        } catch (RetsException e) {
            LogUtil.error(e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public Page<RetsResponse> findComps(Double latitude, Double longitude,
                                        CompsFilterCalculated compsFilter, int page, int size) {
        String sQuery = retsSearchFilterBuilderService.buildRetsQuery(compsFilter, latitude, longitude);

        Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, page, size);
        LogUtil.info("Found {} MLS records for DMQL: {}", retsResponses.getTotalElements(), sQuery);
        for (RetsResponse retsResponse : retsResponses) {
            LogUtil.info(retsResponse.StreetNumber + ", " + retsResponse.StreetName + " " + retsResponse.StreetSuffix + ", " + retsResponse.City);
        }
        return retsResponses;

    }


    @Override
    public CompsResponse findComps(Double latitude, Double longitude, List<CompsFilterCalculated> compsFiltersFilterCalculatedList) {
        final CompsResponse compsResponse = new CompsResponse(null, Collections.emptyList());
        final int[] totalCount = {0};
        Optional<CompsFilterCalculated> usedCompsSearchFilter = compsFiltersFilterCalculatedList.stream().filter(compsFilterCalculated ->
        {
            try {
                String sQuery = retsSearchFilterBuilderService.buildRetsQuery(compsFilterCalculated, latitude, longitude);
                totalCount[0] = getQueryCount(sQuery);
                return totalCount[0] >= 10;
            } catch (RetsException e) {
                LogUtil.error(e.getMessage(), e);
            }
            return false;
        }).findFirst();

        if (usedCompsSearchFilter.isPresent()) {
            CompsFilterCalculated compsFilter = usedCompsSearchFilter.get();

            LogUtil.info("Found {} MLS records for CompsFilter: {}", totalCount[0], compsFilter.toString());

            String sQuery = retsSearchFilterBuilderService.buildRetsQuery(compsFilter, latitude, longitude);
//
            Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, 0, totalCount[0]);
            LogUtil.info("Found {} MLS records for DMQL: {}", retsResponses.getTotalElements(), sQuery);
            compsResponse.setCompsFilterUsed(compsFilter);
            compsResponse.setRetsResponses(retsResponses.getContent());
            return compsResponse;
        }

        return compsResponse;
    }

    public List<RetsResponse> getUpdatedMLSRecords(String matrixUinqueId) {
        String sQuery = "(Matrix_Unique_ID=" + matrixUinqueId + ")";
        Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, RetsResponse.searchFields(), 1, 1);

        return retsResponses.getContent();
    }

    public List<RetsResponse> getUpdatedMLSNumbers(int mlsNumber) {
        String sQuery = "(MLSNumber=" + mlsNumber + ")";
        Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, RetsResponse.searchFields(), 1, 1);

        return retsResponses.getContent();
    }

    @Override
    public RetsResponse getRetsResponseForFields(Integer matrixUinqueId, List<String> fieldsToFix) {
        String sQuery = "(Matrix_Unique_ID=" + matrixUinqueId + ")";
        Page<RetsResponse> retsResponses = queryRetsFeed(sQuery, fieldsToFix.stream().collect(Collectors.joining(",")), 1, 1);

        return retsResponses.getTotalElements() == 1 ? retsResponses.getContent().get(0) : null;
    }
}
