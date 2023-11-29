package com.manning.bippo.dao.filter;

import com.manning.bippo.commons.GeocodeBox;
import com.manning.bippo.commons.GeocodeUtils;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.pojo.NtreisProperty;
//import com.manning.bippo.dao.pojo.NtreisProperty_;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertyCompsSpecification implements Specification<NtreisProperty>
{
    private NtreisProperty ntreisProperty;
    private CompsFilterCalculated compsFilter;

    public PropertyCompsSpecification(NtreisProperty ntreisProperty, CompsFilterCalculated compsFilter)
    {
        this.ntreisProperty = ntreisProperty;
        this.compsFilter = compsFilter;
    }

    @Override
    public Predicate toPredicate(Root<NtreisProperty> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
    {
        final List<Predicate> predicates = new ArrayList<>();
        GeocodeBox boxCorner = GeocodeUtils.getBoxCorner(ntreisProperty.getLatitude().doubleValue(),
                ntreisProperty.getLongitude().doubleValue(), compsFilter.proximityInMiles);

        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(NtreisProperty_.latitude), boxCorner.northEastLatitude.floatValue()));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(NtreisProperty_.latitude), boxCorner.northWestLatitude.floatValue()));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.latitude), boxCorner.southWestLatitude.floatValue()));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.latitude), boxCorner.southEastLatitude.floatValue()));

        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(NtreisProperty_.longitude), boxCorner.northEastLongitude.floatValue()));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.longitude), boxCorner.northWestLongitude.floatValue()));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.longitude), boxCorner.southWestLongitude.floatValue()));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(NtreisProperty_.longitude), boxCorner.southEastLongitude.floatValue()));

        if (compsFilter.getSqftTotalFrom() != null)
        {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.lotSizeAreaSQFT), (float) compsFilter.getSqftTotalFrom()));
        }
        if (compsFilter.getSqftTotalTo() != null)
        {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(NtreisProperty_.lotSizeAreaSQFT), (float) compsFilter.getSqftTotalTo()));

        }

        if (compsFilter.getYearBuiltFrom() != null)
        {
            SingularAttribute yearBuilt = NtreisProperty_.getYearBuilt();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(yearBuilt), compsFilter.getYearBuiltFrom()));
        }
        if (compsFilter.getYearBuiltTo() != null)
        {
            SingularAttribute yearBuilt = NtreisProperty_.getYearBuilt();
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(yearBuilt), compsFilter.getYearBuiltTo()));
        }

        if (compsFilter.getStatus() != null)
        {
            predicates.add(criteriaBuilder.equal(root.get(NtreisProperty_.status), compsFilter.getStatus()));
        }

        if (compsFilter.statusChangeInDays != null)
        {
            Date statusChangeFrom = DateUtils.addDays(new Date(), -compsFilter.statusChangeInDays);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.statusChangeTimestamp), statusChangeFrom));
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

    }
}
