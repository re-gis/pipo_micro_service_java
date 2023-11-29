package com.manning.bippo.service.comps;

import com.google.common.base.Strings;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.*;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.*;
import com.manning.bippo.service.ZipCodeService;
import com.manning.bippo.service.redis.RedisPropertyService;
import com.manning.bippo.service.rets.impl.CompsSearchFilterBuilderService;
import com.manning.bippo.service.rets.pojo.CompsResponse;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CompsServiceImpl extends AbstractCompsServiceImpl implements CompsService
{
    @Autowired
    MLSStatusRepository mlsStatusRepository;

    @Autowired
    MLSPropertySubTypeRepository mlsPropertySubTypeRepository;

    @Autowired
    CompsSearchFilterBuilderService compsSearchFilterBuilderService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    ZipCodeService zipCodeService;

    @Autowired
    PropertyCompsFilterRepository propertyCompsFilterRepository;

    @Autowired
    RedisPropertyService redisPropertyService;

    @Autowired
    CompsWeighingService weighingService;

    @Override
    public CompsResponse getARVComps(NtreisProperty subjectProperty)
    {
        if (subjectProperty != null) {
            return getARVComps("NTREIS", subjectProperty.getPostalCode(), subjectProperty.getSqFtTotal(), subjectProperty.getYearBuilt(),
                    subjectProperty.getPropertyType(), subjectProperty.getPropertySubType(),
                    subjectProperty.getLongitude(), subjectProperty.getLatitude(),
                    subjectProperty.getNumberOfStories() == null ? null : subjectProperty.getNumberOfStories().intValue(),
                    "Y".equalsIgnoreCase(subjectProperty.getPoolYN()));
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    @Override
    public CompsResponse getMaxARVComps(NtreisProperty subjectProperty)
    {
        if (subjectProperty != null) {
            return getMaxARVComps("NTREIS", subjectProperty.getPostalCode(), subjectProperty.getSqFtTotal(), subjectProperty.getYearBuilt(),
                    subjectProperty.getPropertyType(), subjectProperty.getPropertySubType(),
                    subjectProperty.getLongitude(), subjectProperty.getLatitude(),
                    subjectProperty.getNumberOfStories() == null ? null : subjectProperty.getNumberOfStories().intValue(),
                    "Y".equalsIgnoreCase(subjectProperty.getPoolYN()));
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    @Override
    public CompsResponse getARVComps(TrestleProperty subjectProperty)
    {
        if (subjectProperty != null) {
            return getARVComps(subjectProperty.getOriginatingSystemName(), subjectProperty.getPostalCode(), subjectProperty.getLivingArea().floatValue(), subjectProperty.getYearBuilt(),
                    subjectProperty.getPropertyType(), subjectProperty.getPropertySubType(),
                    subjectProperty.getLongitude().floatValue(), subjectProperty.getLatitude().floatValue(),
                    subjectProperty.getStories() == null ? null : subjectProperty.getStories().intValue(),
                    !Strings.isNullOrEmpty(subjectProperty.getPoolFeatures()));
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    @Override
    public CompsResponse getMaxARVComps(TrestleProperty subjectProperty)
    {
        if (subjectProperty != null) {
            return getMaxARVComps(subjectProperty.getOriginatingSystemName(), subjectProperty.getPostalCode(), subjectProperty.getLivingArea().floatValue(), subjectProperty.getYearBuilt(),
                    subjectProperty.getPropertyType(), subjectProperty.getPropertySubType(),
                    subjectProperty.getLongitude().floatValue(), subjectProperty.getLatitude().floatValue(),
                    subjectProperty.getStories() == null ? null : subjectProperty.getStories().intValue(),
                    !Strings.isNullOrEmpty(subjectProperty.getPoolFeatures()));
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    @Override
    public CompsResponse getARVComps(BasicPropertyDetails propertyDetails)
    {
        // TODO Add stories and pool to BasicPropertyDetails and update this call to use those fields
        return getARVComps(propertyDetails.getMlsRegion(), propertyDetails.getPostalCode(),
                propertyDetails.getLivingSize(), propertyDetails.getYearBuiltEffective(),
                propertyDetails.getMlsPropertyType(), propertyDetails.getMlsPropertySubType(),
                propertyDetails.getLongitude(), propertyDetails.getLatitude(), null, null);
    }

    public CompsResponse getMaxARVComps(BasicPropertyDetails propertyDetails)
    {
        return getMaxARVComps(propertyDetails.getMlsRegion(), propertyDetails.getPostalCode(),
                propertyDetails.getLivingSize(), propertyDetails.getYearBuiltEffective(),
                propertyDetails.getMlsPropertyType(), propertyDetails.getMlsPropertySubType(),
                propertyDetails.getLongitude(), propertyDetails.getLatitude(), null, null);
    }

    public CompsResponse getARVComps(String searchRegion, String subjectPostalCode, Float subjectSqFtTotal, Integer subjectYearBuilt,
            String subjectPropertyType, String subjectPropertySubType, Float subjectLongitude, Float subjectLatitude,
            Integer stories, Boolean pool)
    {
        if (!"Residential".equalsIgnoreCase(subjectPropertyType)) {
            // If we don't have a residential property here (e.g. we have a lease), fall back to RES-Single Family comps
            subjectPropertySubType = this.getDefaultSubType(searchRegion);
        }

        LogUtil.debug("No Pre Processed ARV Comps Filter found.");
        Optional<CompsFilterCalculated> usedCompsSearchFilter = this.redisPropertyService
                .getARVCompsFilterUsed(searchRegion, subjectPostalCode, subjectSqFtTotal, subjectYearBuilt,
                        subjectPropertySubType, subjectLongitude, subjectLatitude, stories, pool);

        if (usedCompsSearchFilter.isPresent()) {
            return getARVComps(subjectLongitude, subjectLatitude, usedCompsSearchFilter.get());
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    public CompsResponse getMaxARVComps(String searchRegion, String subjectPostalCode, Float subjectSqFtTotal, Integer subjectYearBuilt,
            String subjectPropertyType, String subjectPropertySubType, Float subjectLongitude, Float subjectLatitude,
            Integer stories, Boolean pool)
    {
        if (!"Residential".equalsIgnoreCase(subjectPropertyType)) {
            // If we don't have a residential property here (e.g. we have a lease), fall back to RES-Single Family comps
            // TODO remap other subtypes correctly?
            subjectPropertySubType = "RES-Single Family";
        }

        LogUtil.debug("No Pre Processed ARV Comps Filter found.");
        Optional<CompsFilterCalculated> maxCompsSearchFilter
                = redisPropertyService.getARVCompsFilterMax(searchRegion, subjectPostalCode,
                subjectSqFtTotal, subjectYearBuilt, subjectPropertySubType,
                subjectLongitude, subjectLatitude, stories, pool);

        if (maxCompsSearchFilter.isPresent()) {
            return getARVComps(subjectLongitude, subjectLatitude, maxCompsSearchFilter.get());
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    public CompsResponse getWholesaleComps(NtreisProperty subject)
    {
        if (subject != null) {
            final Float listPrice = "Leased".equals(subject.getStatus()) ? 0f : subject.getListPrice();
            // In the case of properties for lease, the property subtype will need to be adapted to RES-Single Family find proper comps
            final String subType = !"Residential".equalsIgnoreCase(subject.getPropertyType()) ? "RES-Single Family" : subject.getPropertySubType();

            return getWholesaleComps(subject.getListPrice(), subject.getLongitude(), subject.getLatitude(),
                    subject.getSqFtTotal(), subject.getYearBuilt(), "Sold", subType, "NTREIS");
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    private String getDefaultSubType(String region)
    {
        if (region == null) {
            return "RES-Single Family";
        } else switch (region) {
            case "NTREIS":
                return "RES-Single Family";
            default:
                return "SingleFamilyResidence";
        }
    }

//    private String getSoldStatus(String region)
//    {
//        if (region == null) {
//            return "Sold";
//        } else switch (region) {
//            case "NTREIS":
//                return "Sold";
//            default:
//                return "Closed";
//        }
//    }

    @Override
    public CompsResponse getWholesaleComps(BasicPropertyDetails subject)
    {
        final Float listPrice = "Leased".equals(subject.getMlsStatus()) ? 0f : subject.getListPrice();
        // In the case of properties for lease, the property subtype will need to be adapted to RES-Single Family find proper comps
        final String subType = this.getDefaultSubType(subject.getMlsRegion());
//        final String status = this.getSoldStatus(subject.getMlsRegion());

        return getWholesaleComps(listPrice, subject.getLongitude(), subject.getLatitude(),
                subject.getLivingSize(), subject.getYearBuiltEffective(), "Sold", subType, subject.getMlsRegion());
    }

    public CompsResponse getWholesaleComps(Float subjectListPrice, Float subjectLongitude, Float subjectLatitude,
                                           Float sqftTotal, Integer yearBuilt, String mlsStatus, String mlsSubType, String mlsRegion)
    {
        final CompsFilterCalculated filter = this.compsSearchFilterBuilderService
                .buildDefaultWholesaleFilter(sqftTotal, yearBuilt, mlsStatus, mlsSubType, mlsRegion);

        if (filter != null) {
            return getWholesaleComps(subjectListPrice, subjectLongitude, subjectLatitude, filter);
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    public Optional<CompsFilterCalculated> getARVCompsFilterUsed(NtreisProperty ntreisProperty)
    {
        if (ntreisProperty.getLongitude() != null && ntreisProperty.getLatitude() != null) {
            List<CompsFilterCalculated> compsFiltersFilterCalculatedList = compsSearchFilterBuilderService.buildCompsFilters(zipCodeService.getLocationType(ntreisProperty.getPostalCode()),
                    ntreisProperty.getSqFtTotal(), ntreisProperty.getYearBuilt(), "Sold", Collections.singletonList(ntreisProperty.getPropertySubType()));

            return compsFiltersFilterCalculatedList.stream().filter(compsFilterCalculated -> {
                Date statusChangeFrom = DateUtils.addDays(new Date(), -compsFilterCalculated.statusChangeInDays);
                return ntreisPropertyRepository.countByCompsFilter(compsFilterCalculated.getProximityInMiles(),
                        compsFilterCalculated.getSqftTotalFrom(), compsFilterCalculated.getSqftTotalTo(),
                        compsFilterCalculated.getYearBuiltFrom(), compsFilterCalculated.getYearBuiltTo(),
                        compsFilterCalculated.getStatus(), compsFilterCalculated.getPropertySubType(),
                        statusChangeFrom,
                        ntreisProperty.getLongitude(), ntreisProperty.getLatitude()) > 5;
            }).findFirst();
        } else {
            LogUtil.debug("No Lat/Long co-ordinates found for address: {}", ntreisProperty.getAddress());
        }

        return Optional.empty();
    }

    @Autowired
    DefaultCompRepository defaultCompRepository;

    @Override
    public List<? extends AbstractProperty> getDefaultComps(BasicPropertyDetails subject, CompsResponse compsResponse)
    {
        return this.getDefaultComps(subject, compsResponse.getProperties());
    }

    @Override
    public List<? extends AbstractProperty> getDefaultComps(BasicPropertyDetails subject, List<? extends AbstractProperty> comps)
    {
        /**
         * TODO: Add logic to find if default comps were added previously.
         */
        return bracketArvComps(null/*subject.getSubdivision() TODO add subdivision name to BasicPropertyDetails*/,
                subject.getLivingSize().intValue(), subject.getYearBuiltEffective(),
                subject.getLatitude(), subject.getLongitude(), subject.getApn(), comps);
    }

    @Override
    public List<? extends AbstractProperty> getDefaultComps(TrestleProperty trestleProperty, CompsResponse compsResponse)
    {
        return this.getDefaultComps(trestleProperty, compsResponse.getProperties());
    }

    @Override
    public List<? extends AbstractProperty> getDefaultComps(TrestleProperty trestleProperty, List<? extends AbstractProperty> comps)
    {
        return bracketArvComps(trestleProperty.getSubdivisionName(), trestleProperty.getLivingArea().intValue(),
                    trestleProperty.getYearBuilt(), trestleProperty.getLatitude().floatValue(),
                    trestleProperty.getLongitude().floatValue(), trestleProperty.getParcelNumber(), comps);
    }

    @Override
    public List<? extends AbstractProperty> getDefaultComps(NtreisProperty ntreisProperty, CompsResponse compsResponse)
    {
        return this.getDefaultComps(ntreisProperty, compsResponse.getProperties());
    }

    @Override
    public List<? extends AbstractProperty> getDefaultComps(NtreisProperty ntreisProperty, List<? extends AbstractProperty> comps)
    {
        List<DefaultComp> bySubject = defaultCompRepository.findBySubject(ntreisProperty);

        if (bySubject.isEmpty()) {
            return bracketArvComps(ntreisProperty.getSubdivisionName(), ntreisProperty.getSqFtTotal().intValue(),
                    ntreisProperty.getYearBuilt(), ntreisProperty.getLatitude(),
                    ntreisProperty.getLongitude(), ntreisProperty.getParcelNumber(), comps);
        } else {
            return bySubject.stream().map(DefaultComp::getComp).collect(Collectors.toList());
        }
    }

    /*
     * This is used in ARV calculations, so should return higher valued comps.
     * Formerly, this method returned the three lowest valued comps, and while
     * that is good for wholesale, it was only being used for ARV.
     * In the future, this method should likely be renamed to e.g. buildDefaultHighValuedComps()
     * with a buildDefaultLowValuedComps() for use in wholesale calculations.
     */
//    public List<NtreisProperty> buildDefaultSystemComps(List<NtreisProperty> ntreisProperties)
//    {
//        final List<NtreisProperty> defaultComps = new ArrayList<>();
//        final int use = Math.min(3, ntreisProperties.size());
//        ntreisProperties.sort((left, right) -> right.getListPrice().compareTo(left.getListPrice()));
//        
//        for (int i = 0; i < use; i++) {
//            defaultComps.add(ntreisProperties.get(i));
//        }
//        
//        return defaultComps;
//    }


    public CompsResponse getARVComps(Float longitude, Float latitude, CompsFilterCalculated compsFilter)
    {
        List<? extends AbstractProperty> comps = getCompsWithFilter(longitude, latitude, compsFilter);
        return new CompsResponse(compsFilter, comps);
    }

    public CompsResponse getWholesaleComps(Float subjectListPrice, Float longitude, Float latitude, CompsFilterCalculated filter)
    {
        final float subjectList = subjectListPrice == null ? 0f : subjectListPrice;
        final List<? extends AbstractProperty> comps = getCompsWithFilter(longitude, latitude, filter);

        // Wholesale comp analysis only cares about the bottom of the market, so discard anything that closed for higher than our list
        // But only do this if we would have at least three comps left
        if (subjectList > 0f) {
            final List<? extends AbstractProperty> reducedCopy = new ArrayList<>(comps);
            reducedCopy.removeIf(comp -> comp.getClosePrice() == null || comp.getClosePrice().floatValue() > subjectList);

            if (reducedCopy.size() > 2) {
                return new CompsResponse(filter, reducedCopy);
            }
        }

        return new CompsResponse(filter, comps);
    }

    private List<NtreisProperty> filterOnAverageListPrice(List<NtreisProperty> propertyApiResponses)
    {
        /**
         * If property is within 5% range of the average of the rest of other properties.
         */
        return propertyApiResponses.stream().filter(compsPropertyApiResponse -> {
            OptionalDouble averageListPrice = propertyApiResponses.stream()
                    .filter(otherResponse -> !otherResponse.equals(compsPropertyApiResponse))
                    .collect(Collectors.toList())
                    .stream()
                    .mapToDouble(NtreisProperty::getListPrice)
                    .average();
            return compsPropertyApiResponse.getListPrice() > (averageListPrice.getAsDouble() * 0.95)
                    && compsPropertyApiResponse.getListPrice() < (averageListPrice.getAsDouble() * 1.05);
        }).collect(Collectors.toList());
    }

    @Override
    public CompsResponse getRentalComps(BasicPropertyDetails subjectPropertyDetails)
    {
        Optional<CompsFilterCalculated> usedCompsSearchFilter = redisPropertyService.getRentalCompsFilterUsed(subjectPropertyDetails.getMlsRegion(), subjectPropertyDetails);

        if (usedCompsSearchFilter.isPresent()) {
            CompsFilterCalculated compsFilterCalculated = usedCompsSearchFilter.get();
            List<? extends AbstractProperty> compsWithFilter = getCompsWithFilter(subjectPropertyDetails.getLongitude(), subjectPropertyDetails.getLatitude(), compsFilterCalculated);
            return new CompsResponse(compsFilterCalculated, compsWithFilter);
        }

        return new CompsResponse(null, Collections.emptyList());
    }

    @Override
    public List<? extends AbstractProperty> bracketArvComps(String subjectSubd, int subjectSqFtTotal, int subjectYearBuilt,
            float subjectLongitude, float subjectLatitude, String subjectApn, List<? extends AbstractProperty> comps)
    {
        return this.weighingService.bracketArvComps(subjectSubd, subjectSqFtTotal, subjectYearBuilt, subjectLatitude, subjectLongitude, subjectApn, comps);
    }

    @Override
    public double calculateDistance(Float subjectLatitude, Float subjectLongitude, AbstractProperty compProperty)
    {
        return this.weighingService.calculateDistance(subjectLatitude, subjectLongitude, compProperty);
    }
}
