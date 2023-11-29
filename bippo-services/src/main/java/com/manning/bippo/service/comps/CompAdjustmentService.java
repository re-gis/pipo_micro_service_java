package com.manning.bippo.service.comps;

import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.comps.pojo.CompAdjustments;
import java.util.Collection;
import java.util.List;

public interface CompAdjustmentService
{
    CompAdjustments adjustComp(NtreisProperty subject, AbstractProperty comp, Collection<? extends AbstractProperty> allComps);

    CompAdjustments adjustComp(BasicPropertyDetails subject, AbstractProperty comp, Collection<? extends AbstractProperty> allComps);

    CompAdjustments adjustComp(Number subjectSqft, double sqftPackagePrice,
                               Integer subjectBathsFull, Integer subjectBathsHalf,
                               Integer subjectBedsTotal, Integer subjectParkingSpacesCarport,
                               Integer subejctParkingSpacesGarage, Boolean subjectPool, AbstractProperty comp);
}
