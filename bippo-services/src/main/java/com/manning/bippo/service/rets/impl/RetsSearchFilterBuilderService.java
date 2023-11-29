package com.manning.bippo.service.rets.impl;

import com.manning.bippo.commons.GeocodeBox;
import com.manning.bippo.commons.GeocodeUtils;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.*;
import com.manning.bippo.dao.pojo.City;
import com.manning.bippo.dao.pojo.County;
import com.manning.bippo.dao.pojo.MLSStatus;
import org.apache.commons.lang.time.DateUtils;
import org.realtors.rets.client.RetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetsSearchFilterBuilderService
{
    @Autowired
    MLSStatusRepository mlsStatusRepository;

    @Autowired
    MLSPropertyTypeRepository mlsPropertyTypeRepository;

    @Autowired
    MLSCountyRepository mlsCountyRepository;

    @Autowired
    MLSCityRepository mlsCityRepository;

    public String buildRetsQuery(PropertySearchFilter propertySearchFilter) throws RetsException
    {
        List<String> predicates = buildRetsQueryPredicates(propertySearchFilter);

        return predicates.stream().collect(Collectors.joining(" AND "));
    }

    private List<String> buildRetsQueryPredicates(PropertySearchFilter propertySearchFilter)
    {
        List<String> predicates = new ArrayList<>();

        if (propertySearchFilter.getCity() != null)
        {
            City mlsCity = mlsCityRepository.findByValue(propertySearchFilter.getCity());
            if (mlsCity != null)
            {
                predicates.add("(City=" + mlsCity.getRetsShortValue() + ")");
            } else
            {
                LogUtil.warn("No lookup value found for city: {}", propertySearchFilter.getCity());
            }
        }

        if (propertySearchFilter.getPropertyType() != null)
        {
            propertySearchFilter.getPropertyType().forEach(propertyType -> {
                predicates.add("(PropertyType=" + mlsPropertyTypeRepository.findByValue(propertyType).getRetsShortValue() +")");
            });
        }

        if (propertySearchFilter.getMlsStatus() != null)
        {
            propertySearchFilter.getMlsStatus().forEach(status -> {
                MLSStatus mlsStatus = mlsStatusRepository.findByValue(status);
                if (mlsStatus != null)
                {
                    predicates.add("(Status=" + mlsStatus.getRetsShortValue() + ")");
                }
            });

        }

        if (propertySearchFilter.getNotInMLSStatus() != null)
        {
            StringBuffer notInMLSStatus = new StringBuffer();
            propertySearchFilter.getNotInMLSStatus().forEach(status ->
            {
                MLSStatus mlsStatus = mlsStatusRepository.findByValue(status);
                if (mlsStatus != null)
                {
                    if (notInMLSStatus.length() > 0)
                    {
                        notInMLSStatus.append(",");
                    }
                    notInMLSStatus.append(mlsStatus.getRetsShortValue());
                }
            });
            if (notInMLSStatus.length() > 0)
            {
                predicates.add("(Status=~" + notInMLSStatus.toString() + ")");
            }
        }

        if (propertySearchFilter.getPriceRangeFrom() != null)
        {
            predicates.add("(ClosePrice=" + propertySearchFilter.getPriceRangeFrom() + "+" + ")");
        }
        if (propertySearchFilter.getPriceRangeTo() != null)
        {
            predicates.add("(ClosePrice=" + propertySearchFilter.getPriceRangeTo() + "-" + ")");
        }

        if (propertySearchFilter.getListPriceFrom() != null)
        {
            predicates.add("(ListPrice=" + propertySearchFilter.getListPriceFrom() + "+" + ")");
        }

        if (propertySearchFilter.getListPriceTo() != null)
        {
            predicates.add("(ListPrice=" + propertySearchFilter.getListPriceTo() + "-" + ")");
        }

        if (propertySearchFilter.getCounty() != null)
        {
            County county = mlsCountyRepository.findByValue(propertySearchFilter.getCounty());
            if (county != null)
            {
                predicates.add("(CountyOrParish=" + county.getRetsShortValue() + ")");
            }
        }

        if (propertySearchFilter.getSqftFrom() != null)
        {
            predicates.add("(LotSizeAreaSQFT=" + propertySearchFilter.getSqftFrom() + "+" + ")");
        }

        if (propertySearchFilter.getSqftTo() != null)
        {
            predicates.add("(LotSizeAreaSQFT=" + propertySearchFilter.getSqftTo() + "-" + ")");
        }

        if (propertySearchFilter.getDomFrom() != null)
        {
            predicates.add("(DOM=" + propertySearchFilter.getDomFrom() + "+" + ")");
        }
        if (propertySearchFilter.getDomTo() != null)
        {
            predicates.add("(DOM=" + propertySearchFilter.getDomTo() + "-" + ")");
        }

        if (propertySearchFilter.getCdomFrom() != null)
        {
            predicates.add("(CDOM=" + propertySearchFilter.getCdomFrom() + "+" + ")");
        }
        if (propertySearchFilter.getCdomTo() != null)
        {
            predicates.add("(DOM=" + propertySearchFilter.getCdomTo() + "-" + ")");
        }

        if (propertySearchFilter.getPool() != null)
        {
            String poolChoice = propertySearchFilter.getPool() ? "1" : "0";
            predicates.add("(PoolYN=" + poolChoice + ")");
        }

        if (propertySearchFilter.getBedsFrom() != null)
        {
            predicates.add("(BedsTotal = " + propertySearchFilter.getBedsFrom() + "+" + ")");
        }
        if (propertySearchFilter.getBedsTo() != null)
        {
            predicates.add("(BedsTotal = " + propertySearchFilter.getBedsTo() + "-" + ")");
        }

        if (propertySearchFilter.getBathsFrom() != null)
        {
            predicates.add("(BathsTotal = " + propertySearchFilter.getBathsFrom() + "+" + ")");
        }
        if (propertySearchFilter.getBathsTo() != null)
        {
            predicates.add("(BathsTotal = " + propertySearchFilter.getBathsTo() + "-" + ")");
        }

        if (propertySearchFilter.getHalfBathsFrom() != null)
        {
            predicates.add("(BathsHalf = " + propertySearchFilter.getHalfBathsFrom() + "+" + ")");
        }
        if (propertySearchFilter.getHalfBathsTo() != null)
        {
            predicates.add("(BathsHalf = " + propertySearchFilter.getHalfBathsTo() + "-" + ")");
        }

        if (propertySearchFilter.getHoaFrom() != null)
        {
            predicates.add("(AssociationFee = " + propertySearchFilter.getHoaFrom() + "+" + ")");
        }
        if (propertySearchFilter.getHoaTo() != null)
        {
            predicates.add("(AssociationFee = " + propertySearchFilter.getHoaTo() + "-" + ")");
        }

        if (propertySearchFilter.getStatusChangeTimestampFrom() != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String startTimeFormatted = dateFormat.format(propertySearchFilter.getStatusChangeTimestampFrom()) + "+";
            predicates.add("(MatrixModifiedDT=" + startTimeFormatted + ")");
        }

        if (propertySearchFilter.getStatusChangeTimestampTo() != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String startTimeFormatted = dateFormat.format(propertySearchFilter.getStatusChangeTimestampTo()) + "-";
            predicates.add("(MatrixModifiedDT=" + startTimeFormatted + ")");
        }

        return predicates;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public String buildRetsQuery(CompsFilterCalculated compsFilter, Double latitude, Double longitude)
    {
        List<String> predicates = buildRetsQueryPredicates(compsFilter);
        GeocodeBox boxCorner = GeocodeUtils.getBoxCorner(latitude, longitude, compsFilter.proximityInMiles);

        predicates.add("(Latitude=" + boxCorner.northEastLatitude + "-)");
        predicates.add("(Latitude=" + boxCorner.southEastLatitude + "+)");
        predicates.add("(Longitude=" + boxCorner.northEastLongitude + "-)");
        predicates.add("(Longitude=" + boxCorner.northWestLongitude + "+)");

        return predicates.stream().collect(Collectors.joining(" AND "));
    }

    private List<String> buildRetsQueryPredicates(CompsFilterCalculated compsFilter)
    {
        List<String> predicates = new ArrayList<>();

        if (compsFilter.getSqftTotalFrom() != null)
        {
            predicates.add("(SqFtTotal = " + compsFilter.getSqftTotalFrom() + "+" + ")");
        }
        if (compsFilter.getSqftTotalTo() != null)
        {
            predicates.add("(SqFtTotal = " + compsFilter.getSqftTotalTo() + "-" + ")");
        }

        if (compsFilter.getYearBuiltFrom() != null)
        {
            predicates.add("(YearBuilt = " + compsFilter.getYearBuiltFrom() + "+" + ")");
        }
        if (compsFilter.getYearBuiltTo() != null)
        {
            predicates.add("(YearBuilt = " + compsFilter.getYearBuiltTo() + "-" + ")");
        }

        if (compsFilter.getStatus() != null)
        {
            predicates.add("(Status=" + compsFilter.getStatus() + ")");
        }

        if(compsFilter.getPropertyType() != null)
        {
            predicates.add("(PropertyType=" + compsFilter.getPropertyType() + ")");
        }

        if (compsFilter.statusChangeInDays != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date statusChangeFrom = DateUtils.addDays(new Date(), -compsFilter.statusChangeInDays);
            String startTimeFormatted = dateFormat.format(statusChangeFrom) + "+";
            predicates.add("(MatrixModifiedDT=" + startTimeFormatted + ")");
        }

        return predicates;
    }
}
