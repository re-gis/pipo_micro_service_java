package com.manning.bippo.service.comps;

import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.rets.pojo.CompsResponse;
import java.util.Collection;

import java.util.List;
import java.util.Optional;

public interface CompsService
{
    CompsResponse getARVComps(NtreisProperty ntreisProperty);

    CompsResponse getMaxARVComps(NtreisProperty ntreisProperty);

    CompsResponse getARVComps(TrestleProperty trestleProperty);

    CompsResponse getMaxARVComps(TrestleProperty trestleProperty);

    CompsResponse getARVComps(BasicPropertyDetails propertyDetails);

    CompsResponse getMaxARVComps(BasicPropertyDetails propertyDetails);

    CompsResponse getARVComps(String searchRegion, String subjectPostalCode, Float subjectSqFtTotal, Integer subjectYearBuilt,
            String subjectPropertyType, String subjectPropertySubType,
            Float subjectLongitude, Float subjectLatitude, Integer stories, Boolean pool);

    CompsResponse getARVComps(Float longitude, Float latitude, CompsFilterCalculated compsFilter);

    CompsResponse getWholesaleComps(NtreisProperty ntreisProperty);

    CompsResponse getWholesaleComps(BasicPropertyDetails ntreisProperty);

    CompsResponse getWholesaleComps(Float subjectListPrice, Float subjectLongitude, Float subjectLatitude,
                                    Float sqftTotal, Integer yearBuilt, String mlsStatus, String mlsSubType, String mlsRegion);

//    CompsResponse getWholesaleComps(NtreisProperty ntreisProperty, CompsFilterCalculated compsFilter);

    List<? extends AbstractProperty> bracketArvComps(String subdivisionName, int subjectSqFtTotal, int subjectYearBuilt,
            float subjectLatitude, float subjectLongitude, String subjectApn, List<? extends AbstractProperty> comps);

    List<? extends AbstractProperty> getDefaultComps(NtreisProperty ntreisProperty, CompsResponse compsResponse);

    List<? extends AbstractProperty> getDefaultComps(TrestleProperty trestleProperty, CompsResponse compsResponse);

    List<? extends AbstractProperty> getDefaultComps(BasicPropertyDetails ntreisProperty, CompsResponse compsResponse);

    List<? extends AbstractProperty> getDefaultComps(NtreisProperty ntreisProperty, List<? extends AbstractProperty> comps);

    List<? extends AbstractProperty> getDefaultComps(TrestleProperty trestleProperty, List<? extends AbstractProperty> comps);

    List<? extends AbstractProperty> getDefaultComps(BasicPropertyDetails ntreisProperty, List<? extends AbstractProperty> comps);

    Optional<CompsFilterCalculated> getARVCompsFilterUsed(NtreisProperty ntreisProperty);

    List<? extends AbstractProperty> getCompsWithFilter(Float longitude, Float latitude, CompsFilterCalculated compsFilter);

    CompsResponse getRentalComps(BasicPropertyDetails propertyDetails);

    double calculateDistance(Float subjectLatitude, Float subjectLongitude, AbstractProperty compProperty);
}
