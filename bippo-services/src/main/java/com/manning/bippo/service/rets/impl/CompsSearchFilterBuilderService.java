package com.manning.bippo.service.rets.impl;

import com.google.common.collect.Lists;
import com.manning.bippo.commons.dto.CompsFilter;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CompsSearchFilterBuilderService
{
    public static final CompsFilterBuilder URBAN_BUILDER = new CompsFilterBuilder(1, 1, .25d, 20, 25, 5, 180, 360, 90);
    public static final CompsFilterBuilder SUBURBAN_BUILDER = new CompsFilterBuilder(1, 2, .25d, 20, 25, 5, 180, 360, 90);
    public static final CompsFilterBuilder RURAL_BUILDER = new CompsFilterBuilder(1, 10, 1d, 20, 25, 5, 180, 360, 180);

    public List<CompsFilter> buildCompsFilters(String areaType)
    {
        switch (areaType.toLowerCase())
        {
            case "urban":
                return buildUrbanCompsFilter();
            case "suburban":
                return buildSubUrbanCompsFilter();
            case "rural":
            default:
                return buildRuralCompsFilter();
        }
    }

    private List<CompsFilter> buildRuralCompsFilter()
    {
        List<CompsFilter> compsFilterCalculatedList = new ArrayList<>();
        RURAL_BUILDER.build(compsFilterCalculatedList);
        return compsFilterCalculatedList;
    }

    private List<CompsFilter> buildSubUrbanCompsFilter()
    {
        List<CompsFilter> compsFilterCalculatedList = new ArrayList<>();
        SUBURBAN_BUILDER.build(compsFilterCalculatedList);
        return compsFilterCalculatedList;
    }

    private List<CompsFilter> buildUrbanCompsFilter()
    {
        List<CompsFilter> compsFilterCalculatedList = new ArrayList<>();
        URBAN_BUILDER.build(compsFilterCalculatedList);
        return compsFilterCalculatedList;
    }

    private CompsFilter buildCompsFilter(double proximityInMiles, int glaPercentWithIn, int statusChangeInDays)
    {
        CompsFilter compsFilter = new CompsFilter();
        compsFilter.statusChangeInDays = statusChangeInDays;
        compsFilter.sqftTotalWithIn = glaPercentWithIn;
        compsFilter.proximityInMiles = proximityInMiles;
        return compsFilter;
    }

    public List<CompsFilterCalculated> buildCompsFilters(String areaType, Float sqftTotal, Integer yearBuilt, String mlsStatus, List<String> propertySubTypeRetsShortValue)
    {
        return this.buildCompsFilters(areaType, sqftTotal, yearBuilt, mlsStatus, propertySubTypeRetsShortValue, null, null, null);
    }

    public CompsFilterCalculated maxCompsFilter(String areaType, Float sqftTotal, Integer yearBuilt, String mlsStatus,
            List<String> propertySubTypeRetsShortValue, Integer stories, Boolean pool)
    {
        final List<CompsFilter> compsFilters = buildCompsFilters(areaType);

        if (compsFilters.isEmpty()) {
            return null;
        }

        final CompsFilter lastFilter = compsFilters.get(compsFilters.size() - 1);
        final int currentYear = LocalDate.now().get(ChronoField.YEAR);

        {
            final int subjectAge = currentYear - yearBuilt;
            final int years;

            if (subjectAge <= 10) {
                years = 5;
            } else if (subjectAge <= 30) {
                years = 10;
            } else if (subjectAge <= 50) {
                years = 15;
            } else if (subjectAge <= 75) {
                years = 20;
            } else {
                years = 25;
            }

            for (CompsFilter filter : compsFilters) {
                filter.yearBuiltWithIn = Integer.valueOf(years);
            }
        }

        if (yearBuilt >= currentYear - 1)
        {
            // New properties (defined as having been built in the last 2 years) should only be compared against other new properties
            CompsFilterCalculated compsFilterCalculated = new CompsFilterCalculated(lastFilter);
            compsFilterCalculated.setSqftTotal(sqftTotal);
            compsFilterCalculated.setYearBuiltFrom(currentYear - 1);
            compsFilterCalculated.setYearBuiltTo(currentYear + compsFilterCalculated.yearBuiltWithIn);
            compsFilterCalculated.setStatus(mlsStatus);
            compsFilterCalculated.setPropertySubType(propertySubTypeRetsShortValue);
            compsFilterCalculated.setStories(stories);
            compsFilterCalculated.setPool(pool);
            return compsFilterCalculated;
        } else
        {
            CompsFilterCalculated compsFilterCalculated = new CompsFilterCalculated(lastFilter);
            compsFilterCalculated.setSqftTotal(sqftTotal);
            compsFilterCalculated.setYearBuilt(yearBuilt);

            if (compsFilterCalculated.getYearBuiltTo() > currentYear - 1)
            {
                // Ensure we do not capture any new properties as comps for this non-new subject
                compsFilterCalculated.setYearBuiltTo(currentYear - 1);
            }

            compsFilterCalculated.setStatus(mlsStatus);
            compsFilterCalculated.setPropertySubType(propertySubTypeRetsShortValue);
            compsFilterCalculated.setStories(stories);
            compsFilterCalculated.setPool(pool);
            return compsFilterCalculated;
        }
    }

    public List<CompsFilterCalculated> buildCompsFilters(String areaType, Float sqftTotal, Integer yearBuilt, String mlsStatus,
            List<String> propertySubTypeRetsShortValue, Integer stories, Boolean pool, String region)
    {
        final List<CompsFilter> compsFilters = buildCompsFilters(areaType);
        final List<CompsFilterCalculated> compsFilterCalculatedList = new ArrayList<>();
        final int currentYear = LocalDate.now().get(ChronoField.YEAR);
        
        {
            final int subjectAge = currentYear - yearBuilt;
            int years;

            if (subjectAge <= 10) {
                years = 5;
            } else if (subjectAge <= 30) {
                years = 10;
            } else if (subjectAge <= 50) {
                years = 15;
            } else if (subjectAge <= 75) {
                years = 20;
            } else {
                years = 25;
            }

            if (region != null) {
                switch (region) {
                    case "HAR":
                        // These per-region idiosyncrasies should be more well defined
                        years *= 5;
                        break;
                    default:
                        break;
                }
            }

            for (CompsFilter filter : compsFilters) {
                filter.yearBuiltWithIn = Integer.valueOf(years);
            }
        }

        if (yearBuilt >= currentYear - 1) {
            // New properties (defined as having been built in the last 2 years) should only be compared against other new properties
            compsFilters.forEach(compsFilter -> {
                CompsFilterCalculated compsFilterCalculated = new CompsFilterCalculated(compsFilter);
                compsFilterCalculated.setSqftTotal(sqftTotal);
                compsFilterCalculated.setYearBuiltFrom(currentYear - 1);
                compsFilterCalculated.setYearBuiltTo(currentYear + compsFilterCalculated.yearBuiltWithIn);
                compsFilterCalculated.setStatus(mlsStatus);
                compsFilterCalculated.setPropertySubType(propertySubTypeRetsShortValue);
                compsFilterCalculated.setStories(stories);
                compsFilterCalculated.setPool(pool);
                compsFilterCalculatedList.add(compsFilterCalculated);
            });
        } else {
            compsFilters.forEach(compsFilter -> {
                CompsFilterCalculated compsFilterCalculated = new CompsFilterCalculated(compsFilter);
                compsFilterCalculated.setSqftTotal(sqftTotal);
                compsFilterCalculated.setYearBuilt(yearBuilt);

                if (compsFilterCalculated.getYearBuiltTo() > currentYear - 1) {
                    // Ensure we do not capture any new properties as comps for this non-new subject
                    compsFilterCalculated.setYearBuiltTo(currentYear - 1);
                }

                compsFilterCalculated.setStatus(mlsStatus);
                compsFilterCalculated.setPropertySubType(propertySubTypeRetsShortValue);
                compsFilterCalculated.setStories(stories);
                compsFilterCalculated.setPool(pool);
                compsFilterCalculatedList.add(compsFilterCalculated);
            });
        }

        return compsFilterCalculatedList;
    }

    public CompsFilterCalculated buildDefaultWholesaleFilter(Float sqftTotal, Integer yearBuilt, String mlsStatus, String propertySubType, String mlsRegion)
    {
        final CompsFilterCalculated filter = new CompsFilterCalculated(buildCompsFilter(3d, 25, 180));
        filter.setSqftTotal(sqftTotal);
        filter.setYearBuilt(yearBuilt);
        filter.setStatus(mlsStatus);
        filter.setPropertySubType(Lists.newArrayList(propertySubType, null));
        filter.region = mlsRegion;
        return filter;
    }

    /**
     * @param areaType
     * @param sqftTotal
     * @param yearBuilt
     * @param mlsStatus          default to null
     * @param propertySubTypeRetsShortValue default to emptyList
     * @return
     */
    public List<CompsFilterCalculated> buildLeaseCompFilters(String areaType, Float sqftTotal, Integer yearBuilt, String mlsStatus, String propertyType, List<String> propertySubTypeRetsShortValue)
    {

        List<CompsFilter> compsFilters = buildCompsFilters(areaType);
        List<CompsFilterCalculated> compsFilterCalculatedList = new ArrayList<>();

        compsFilters.forEach(compsFilter -> {
            compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, compsFilter));
        });

//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(4, 20, 365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(5, 20, 365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(3, 20, 2*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(4, 20, 2*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(5, 20, 2*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(3, 20, 3*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(4, 20, 3*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(5, 20, 3*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(3, 20, 4*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(4, 20, 4*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(5, 20, 4*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, yearBuilt, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(3, 20, 5*365)));

//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, null, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(15, 20, 365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, null, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(15, 20, 2*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, null, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(15, 20, 3*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, null, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(15, 20, 4*365)));
//        compsFilterCalculatedList.add(buildLeaseCompsFilerCalculated(sqftTotal, null, mlsStatus, propertyType, propertySubTypeRetsShortValue, buildCompsFilter(15, 20, 5*365)));

        return compsFilterCalculatedList;
    }

    private CompsFilterCalculated buildLeaseCompsFilerCalculated(Float sqftTotal, Integer yearBuilt, String mlsStatus, String propertyType, List<String> propertySubTypeRetsShortValue, CompsFilter compsFilter)
    {
        CompsFilterCalculated compsFilterCalculated = new CompsFilterCalculated(compsFilter);
        compsFilterCalculated.setPropertyType(propertyType);
        compsFilterCalculated.setStatus(mlsStatus);
        compsFilterCalculated.setSqftTotal(sqftTotal);
        compsFilterCalculated.setYearBuilt(yearBuilt);
        compsFilterCalculated.setPropertySubType(propertySubTypeRetsShortValue);
        return compsFilterCalculated;
    }
}
