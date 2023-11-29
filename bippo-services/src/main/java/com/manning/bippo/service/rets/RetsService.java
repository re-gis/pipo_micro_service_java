package com.manning.bippo.service.rets;

import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.rets.pojo.RetsResource;
import com.manning.bippo.service.rets.pojo.CompsResponse;
import com.manning.bippo.service.rets.pojo.RetsResponse;
import org.realtors.rets.client.GetObjectResponse;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.common.metadata.Metadata;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RetsService
{
    List<RetsResponse> findMLSRecord(String primaryNumber, String streetName, String streetSuffix,
                                     String cityName, Integer zipCode);

    RetsResource getResourceMetadata(String resourceName) throws RetsException;

    List<RetsResponse> getSoldMLSRecords(Date startTime, Date endTime);

    List<RetsResponse> getUpdatedMLSRecords(Date startTime, Date endTime);

    GetObjectResponse getAllPhotos(Set<String> retsId, String objectType) throws RetsException;

    GetObjectResponse getFrontPhoto(Set<String> retsId, String objectType) throws RetsException;

    Page<RetsResponse> findBySearchFilter(PropertySearchFilter propertySearchFilter, int page, int size);

    Page<RetsResponse> findNewBySearchFilter(PropertySearchFilter propertySearchFilter, Date startTime, Date endTime, int page, int size);

    int getNewResultsCountBySearchFilter(PropertySearchFilter propertySearchFilter, Date startTime, Date endTime);

    int getTotalResultsCountBySearchFilter(PropertySearchFilter propertySearchFilter);

    Metadata getMetadata() throws RetsException;

    Page<RetsResponse> findComps(Double latitiude, Double longitude,
                                 CompsFilterCalculated compsFilter, int page, int size);

    CompsResponse findComps(Double latitude, Double longitude, List<CompsFilterCalculated> compsFiltersFilterCalculatedList);

    RetsResponse getRetsResponseForFields(Integer matrixUinqueId, List<String> fieldsToFix);

    List<RetsResponse> getUpdatedMLSRecords(String matrixUinqueId);

    List<RetsResponse> getUpdatedMLSNumbers(int mlsNumber);
}
