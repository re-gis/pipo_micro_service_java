package com.manning.bippo.service.mapping;

import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.pojo.NtreisPhoto;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.rets.pojo.RetsResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;

public interface RetsBippoMappingService
{
    @Async
    List<NtreisProperty> mapRetsResponseToNtreisProperty(List<RetsResponse> retsResponses);

    @Async
    NtreisProperty mapRetsResponseToNtreisProperty(RetsResponse ntreisResponse);

    NtreisProperty mapRetsResponseToNtreisProperty(RetsResponse retsResponse, boolean reImport);

    List<NtreisProperty> importNtreisProperties(String firstLine, String lastLine, List<NtreisProperty> ntreisProperties);

    List<NtreisProperty> importMLSRecord(String firstLine, String lastLine, String primaryNumber,
                                         String streetName, String streetSuffix,
                                         String cityName, Integer zipCode);

    List<NtreisProperty> importUpdatedMLSRecords(Date startTime, Date endTime);

    List<NtreisProperty> importUpdatedMLSRecords(String matrixUniqueId, boolean semanticize);

    List<NtreisProperty> importUpdatedMLSRecords(int mlsNumber);

    List<NtreisProperty> importSoldMLSRecords(Date startTime, Date endTime);

    void getFrontPhotos(List<Long> propertyIds);

    void getAllPhotos(List<Long> propertyIds);

    void markPhotosDoNotCheck(List<Long> propertyIds);

    void importByFilter(PropertySearchFilter retsSearch);

    void importComps(NtreisProperty subject, List<CompsFilterCalculated> compsFilter, String type);

    void importComps(BasicPropertyDetails subject, List<CompsFilterCalculated> compsFilter, String type);
}
