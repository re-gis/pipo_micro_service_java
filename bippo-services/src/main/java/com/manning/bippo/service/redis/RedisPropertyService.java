package com.manning.bippo.service.redis;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.SearchResultsRecord;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.redis.dao.AbstractRedisRepository;
import com.manning.bippo.redis.dao.RedisPropertyRepository;
import com.manning.bippo.redis.dao.RedisPropertyRepositoryHAR;
import com.manning.bippo.redis.pojo.AbstractRedisProperty;
import com.manning.bippo.redis.pojo.RedisProperty;
import com.manning.bippo.redis.pojo.RedisPropertyHAR;
import com.manning.bippo.service.ZipCodeService;
import com.manning.bippo.service.rets.impl.CompsSearchFilterBuilderService;
import com.manning.bippo.service.trestle.TrestleDataService;
import com.manning.bippo.service.trestle.TrestleDataUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RedisPropertyService
{
    @Autowired
    RedisPropertyRepository repositoryNtreis;

    @Autowired
    RedisPropertyRepositoryHAR repositoryHAR;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    TrestleDataService trestleDataService;

    public void savePropertyToRedis(List<NtreisProperty> ntreisProperty)
    {
        List<RedisProperty> collect = ntreisProperty.stream()
                .filter(p -> p.getLongitude() != null && p.getLatitude() != null)
                .map(RedisPropertyService::buildRedisProperty)
                .collect(Collectors.toList());
        LogUtil.debug("Saving {}/{} NTREIS properties to redis.", collect.size(), ntreisProperty.size());

        this.repositoryNtreis.save(collect);
    }

    public void savePropertyToRedis(NtreisProperty ntreisProperty)
    {
        if (ntreisProperty.getLongitude() != null && ntreisProperty.getLongitude() != null) {
            RedisProperty redisProperty = buildRedisProperty(ntreisProperty);
            LogUtil.debug("Saving property to redis: {}, {}, {}", ntreisProperty.getAddress(), ntreisProperty.getLongitude(), ntreisProperty.getLatitude());
            this.repositoryNtreis.save(redisProperty);
        }
    }

    public void debugRedisCaches() {
        LogUtil.debug("NTREIS Redis: {} entries", this.repositoryNtreis.count());
        LogUtil.debug("HAR Redis: {} entries", this.repositoryHAR.count());
    }

    public void truncateByRegion(String mlsRegion) {
        switch (mlsRegion) {
            case "NTREIS":
                this.repositoryNtreis.deleteAll();
                break;
            case "HAR":
                this.repositoryHAR.deleteAll();
                break;
        }
    }

    public void truncateByRegionAndTime(String mlsRegion, Date truncateBefore) {
        final long before, after;

        switch (mlsRegion) {
            case "NTREIS":
                before = this.repositoryNtreis.count();
                this.repositoryNtreis.deleteByStatusChangeTimestampBefore(truncateBefore);
                after = this.repositoryNtreis.count();
                break;
            case "HAR":
                before = this.repositoryHAR.count();
                this.repositoryHAR.deleteByStatusChangeTimestampBefore(truncateBefore);
                after = this.repositoryHAR.count();
                break;
            default:
                return;
        }

        LogUtil.debug("Truncated {} entries from {} ({} -> {})", after - before, mlsRegion, before, after);
    }

    public void saveTrestlePropertyToRedis(List<? extends TrestleProperty> trestleProperty)
    {
        final List<? extends TrestleProperty> copy = new ArrayList<>(trestleProperty);
        // Remove any properties without complete lat/long and GLA
        copy.removeIf(RedisPropertyService::excludeTrestlePropertyForRedis);

        if (copy.isEmpty()) {
            return;
        }

        final String region = copy.iterator().next().getOriginatingSystemName();
        LogUtil.debug("Saving {}/{} Trestle {} properties to redis.", copy.size(), trestleProperty.size(), region);

        switch (region) {
            case "HAR":
                this.repositoryHAR.save(copy.stream().map(RedisPropertyService::buildRedisPropertyHAR).collect(Collectors.toList()));
                break;
            default:
                LogUtil.warn("Unrecognized Trestle region for redis data: " + region);
                return;
        }
    }

    public void saveTrestlePropertyToRedis(TrestleProperty trestleProperty)
    {
        if (trestleProperty.getLongitude() != null && trestleProperty.getLongitude() != null) {
            LogUtil.debug("Saving property to redis: {}, {}, {}", trestleProperty.getAddress(), trestleProperty.getLongitude(), trestleProperty.getLatitude());

            switch (trestleProperty.getOriginatingSystemName()) {
                case "HAR":
                    this.repositoryHAR.save(buildRedisPropertyHAR(trestleProperty));
                    break;
                default:
                    LogUtil.warn("Unrecognized Trestle region for redis data: " + trestleProperty.getOriginatingSystemName());
                    return;
            }
        }
    }

    private static RedisProperty buildRedisProperty(NtreisProperty ntreisProperty)
    {
        return RedisProperty.builder()
                .id(ntreisProperty.getId())
//                        .propertyType(ntreisProperty.getPropertyType())
                .propertySubType(ntreisProperty.getPropertySubType())
                .location(new Point(ntreisProperty.getLongitude(), ntreisProperty.getLatitude()))
                .mlsStatus(ntreisProperty.getStatus())
                //                    .streetNumber(ntreisProperty.getStreetNumber())
                //                    .streetName(ntreisProperty.getStreetName())
                //                    .streetSuffix(ntreisProperty.getStreetSuffix())
                //                    .city(ntreisProperty.getCity())
                //                    .stateOrProvince(ntreisProperty.getStateOrProvince())
                //                    .postalCode(ntreisProperty.getPostalCode())
                .yearBuilt(ntreisProperty.getYearBuilt())
                .sqftTotal(ntreisProperty.getSqFtTotal())
                .storyCount(ntreisProperty.getNumberOfStories() == null ? 1 : ntreisProperty.getNumberOfStories().intValue())
                .poolState("Y".equalsIgnoreCase(ntreisProperty.getPoolYN()))
                .statusChangeTimestamp(ntreisProperty.getStatusChangeTimestamp())
                .build();
    }

    private static boolean excludeTrestlePropertyForRedis(TrestleProperty p) {
        if (p.getLongitude() == null || p.getLatitude() == null || p.getLivingArea() == null || p.getPropertyType() == null || isInvalidLatLong(p.getLatitude(), p.getLongitude())) {
            return true;
        } else switch (p.getPropertyType()) {
            case "Residential":
                return p.getClosePrice() != null && p.getClosePrice().doubleValue() < 10000d;
            case "ResidentialLease":
                return p.getClosePrice() != null && p.getClosePrice().doubleValue() > 20000d;
            default:
                return true;
        }
    }

    private static boolean isInvalidLatLong(Double latitude, Double longitude) {

        if (Double.compare(latitude, -90f) <= 0) {
            return true;
        }

        if (Double.compare(latitude, 90f) >= 0) {
            return true;
        }

        if (Double.compare(longitude, -180f) <= 0) {
            return true;
        }

        if (Double.compare(longitude, 180f) >= 0) {
            return true;
        }
        return false;
    }

    private static RedisPropertyHAR buildRedisPropertyHAR(TrestleProperty trestleProperty)
    {
        return RedisPropertyHAR.builder()
                .id(trestleProperty.getId())
                .propertySubType(trestleProperty.getPropertySubType())
                .location(new Point(trestleProperty.getLongitude(), trestleProperty.getLatitude()))
                .mlsStatus(TrestleDataUtils.mapTrestleStatusToNtreis(trestleProperty.getPropertyType(), trestleProperty.getStandardStatus()))
                .yearBuilt(trestleProperty.getYearBuilt())
                .sqftTotal(trestleProperty.getLivingArea() == null ? 0f : trestleProperty.getLivingArea().floatValue())
                .storyCount(trestleProperty.getStories() == null ? 1 : trestleProperty.getStories())
                .poolState(!Strings.isNullOrEmpty(trestleProperty.getPoolFeatures()))
                .statusChangeTimestamp(trestleProperty.getStatusChangeTimestamp())
                .build();
    }


    @Autowired
    CompsSearchFilterBuilderService compsSearchFilterBuilderService;

    @Autowired
    ZipCodeService zipCodeService;


    public Optional<CompsFilterCalculated> getARVCompsFilterUsed(String region, NtreisProperty ntreisProperty)
    {

        return getARVCompsFilterUsed(region, ntreisProperty.getPostalCode(),
                ntreisProperty.getSqFtTotal(),
                ntreisProperty.getYearBuilt(),
                ntreisProperty.getPropertySubType(),
                ntreisProperty.getLongitude(), ntreisProperty.getLatitude(),
                ntreisProperty.getNumberOfStories() == null ? null : ntreisProperty.getNumberOfStories().intValue(),
                "Y".equalsIgnoreCase(ntreisProperty.getPoolYN()));
    }

    public Optional<CompsFilterCalculated> getARVCompsFilterMax(String region, String subjectPostalCode, Float subjectSqFtTotal,
                                                                 Integer subjectYearBuilt, String subjectPropertySubType,
                                                                 Float subjectLongitude, Float subjectLatitude,
                                                                 Integer stories, Boolean pool)
    {
        // TODO: consider the same abstraction mentioned in getARVCompsFilterUsed
        final String areaType = findAreaType(region, subjectPostalCode);
        final List<String> subTypes = getDefaultApplicableSubTypes(region, subjectPropertySubType);

        CompsFilterCalculated maxCompsFilter = compsSearchFilterBuilderService.maxCompsFilter(
                areaType, subjectSqFtTotal, subjectYearBuilt, "Sold", subTypes, stories, pool);

        if (maxCompsFilter == null) {
            return Optional.empty();
        }

        maxCompsFilter.region = region;
        int filterCount = countByFilter(region, subjectLongitude, subjectLatitude, maxCompsFilter, true);

        if (filterCount <= 6) {
            // Ensure the filter satisfies the constraints applied to getARVCompsFilterUsed(..)
            return Optional.empty();
        }
        return Optional.of(maxCompsFilter);
    }

    public String findAreaType(String region, String subjectPostalCode) {
        if (region != null) {
            switch (region) {
                case "HAR":
                    // Force HAR to be interpreted as suburban until we can map the specific zip codes
                    return "suburban";
            }
        }

        return this.zipCodeService.getLocationType(subjectPostalCode);
    }

    public List<String> getDefaultApplicableSubTypes(String region, String subjectPropertySubType) {
        if (region != null) {
            switch (region) {
                case "HAR":
//                    return null;
                    return Lists.newArrayList("SingleFamilyResidence", "Townhouse", "Condominium", "Detached", "Duplex", null);
            }
        }

        return Collections.singletonList(subjectPropertySubType);
    }

    public Optional<CompsFilterCalculated> getARVCompsFilterUsed(String region, String subjectPostalCode, Float subjectSqFtTotal,
                                                                 Integer subjectYearBuilt, String subjectPropertySubType,
                                                                 Float subjectLongitude, Float subjectLatitude,
                                                                 Integer stories, Boolean pool)
    {
        final StopWatch timer = new StopWatch();
        timer.start();

        /*
         * TODO It may be a good idea to abstract this class out into e.g.
         *      RedisPropertyServiceBase, RedisPropertyServiceTrestle, ..
         *      to address the need to provide some default values per-region,
         *      such as area type defaults, or property subtype defaults.
         */

        final String areaType = findAreaType(region, subjectPostalCode);
        final List<String> subTypes = getDefaultApplicableSubTypes(region, subjectPropertySubType);
        final List<CompsFilterCalculated> candidateFilters = compsSearchFilterBuilderService.buildCompsFilters(
                areaType, subjectSqFtTotal, subjectYearBuilt, "Sold", subTypes, stories, pool, region);
        CompsFilterCalculated bestFound = null;
        int maxCount = 0;

        for (CompsFilterCalculated filter : candidateFilters) {
            final int filterCount;
            filter.region = region;

            if ((filterCount = countByFilter(region, subjectLongitude, subjectLatitude, filter, true)) >= 6) {
                // Break immediately and use this filter if we find >= 6 comps
                maxCount = filterCount;
                bestFound = filter;
                break;
            } else if (filterCount > maxCount) {
                // If we couldn't find >= 6 comps, we'll return the first of any filters that otherwise produced the most results
                maxCount = filterCount;
                bestFound = filter;
            }
        }

        timer.stop();
        LogUtil.info("RedisPropertyService.getARVCompsFilterUsed took {} msec.", timer.getTime());

        // If we don't have at least 3 comps, mark the filter as failed
        if (bestFound != null && maxCount < 3) {
            bestFound.failed = true;
        }

        return Optional.ofNullable(bestFound);
    }

    public Optional<CompsFilterCalculated> getRentalCompsFilterUsed(String region, BasicPropertyDetails subject)
    {
        StopWatch timer = new StopWatch();
        timer.start();

        final String propertyType;
        final List<String> propertySubTypes;

        switch (region == null ? "NTREIS" : region) {
            case "NTREIS":
                propertyType = "Residential Lease";
                propertySubTypes = Lists.newArrayList("LSE-Apartment", "LSE-Condo/Townhome",
                        "LSE-Duplex", "LSE-Fourplex", "LSE-House", "LSE-Mobile","LSE-Triplex");
                break;
            default:
                 propertyType = "ResidentialLease";
                 propertySubTypes = null;
                 break;
        }

        final List<CompsFilterCalculated> rentalCompsFilter = compsSearchFilterBuilderService.buildLeaseCompFilters(
                findAreaType(region, subject.getPostalCode()), subject.getLivingSize(),
                subject.getYearBuiltEffective(), "Leased", propertyType, propertySubTypes);
        CompsFilterCalculated bestFound = null;
        int maxCount = 0;

        for (CompsFilterCalculated filter : rentalCompsFilter) {
            final int filterCount;
            filter.region = region;

            if ((filterCount = countByFilter(region, subject.getLongitude(), subject.getLatitude(), filter, true)) >= 6) {
                // Break immediately and use this filter if we find >= 6 comps
                maxCount = filterCount;
                bestFound = filter;
                break;
            } else if (filterCount > maxCount) {
                // If we couldn't find >= 6 comps, we'll return the first of any filters that otherwise produced the most results
                maxCount = filterCount;
                bestFound = filter;
            }
        }

        timer.stop();
        LogUtil.info("RedisPropertyService.getARVCompsFilterUsed took {} msec.", timer.getTime());

        // If we don't have at least 3 comps, mark the filter as failed
        if (bestFound != null && maxCount < 3) {
            bestFound.failed = true;
        }

        return Optional.ofNullable(bestFound);
    }


    public int countByFilter(String region, Float subjectLongitude, Float subjectLatitude, CompsFilterCalculated compsFilterCalculated, boolean log)
    {
        final AbstractRedisRepository repo;

        if (region == null) {
            region = "NTREIS";
        }

        switch (region) {
            case "HAR":
                repo = this.repositoryHAR;
                break;
            case "NTREIS":
            default:
                repo = this.repositoryNtreis;
                break;
        }

        return getCompIds(repo, subjectLongitude, subjectLatitude, compsFilterCalculated, log).size();
    }

    public List<NtreisProperty> findByFilter(Float subjectLongitude, Float subjectLatitude, CompsFilterCalculated filter)
    {
        final StopWatch timer = new StopWatch();
        timer.start();

        final List<NtreisProperty> comps = this.ntreisPropertyRepository.findAll(
                getCompIds(this.repositoryNtreis, subjectLongitude, subjectLatitude, filter, false));

        timer.stop();
        LogUtil.info("RedisPropertyService.findByFilter (NTREIS) took {} msec.", timer.getTime());
        return comps;
    }

    public List<? extends AbstractProperty> findByFilterInfer(String region, Float subjectLongitude, Float subjectLatitude, CompsFilterCalculated filter)
    {
        if (region == null) {
            region = "NTREIS";
        }

        switch (region) {
            case "HAR":
                return this.findByFilterHAR(subjectLongitude, subjectLatitude, filter);
            case "NTREIS":
            default:
                return this.findByFilter(subjectLongitude, subjectLatitude, filter);
        }
    }

    public List<? extends TrestleProperty> findByFilterHAR(Float subjectLongitude, Float subjectLatitude, CompsFilterCalculated filter)
    {
        final StopWatch timer = new StopWatch();
        timer.start();

        final List<? extends TrestleProperty> comps = this.trestleDataService.findByRegionAndIds("HAR",
                getCompIds(this.repositoryHAR, subjectLongitude, subjectLatitude, filter, false));

        timer.stop();
        LogUtil.info("RedisPropertyService.findByFilter (HAR) took {} msec.", timer.getTime());
        return comps;
    }

    private static List<Long> getCompIds(AbstractRedisRepository repo, Float subjectLongitude, Float subjectLatitude, CompsFilterCalculated filter, boolean log)
    {
        final List<? extends AbstractRedisProperty> byLocationNear = repo.findByLocationNear(
                new Point(subjectLongitude, subjectLatitude), new Distance(filter.getProximityInMiles(), Metrics.MILES));
        final List<Long> passedFilter = new ArrayList<>();

        if (byLocationNear.size() > 10000) {
            LogUtil.warn("Aborting: too many results ({}) for filter ({}, {}) {}", byLocationNear.size(), subjectLatitude, subjectLongitude, filter);

            if (filter.primed) {
                filter.resultsDetails = new SearchResultsRecord(byLocationNear.size(), 0, 0, 0, 0, 0, 0, 0, 0);
            }

            return passedFilter;
        }

        final Date after = DateUtils.addDays(new Date(), -filter.getStatusChangeInDays());
        final Set<String> rStatuses = new HashSet<>();
        int timestamp = 0, status = 0, subtype = 0, year = 0, sqft = 0, pool = 0, stories = 0;

        for (AbstractRedisProperty prop : byLocationNear) {
            // Require the RedisProperty to define a StatusChangeTimestamp after our required Date
            if (prop.getStatusChangeTimestamp() == null || !prop.getStatusChangeTimestamp().after(after)) {
                timestamp++;
                continue;
            }

            if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase(prop.getMlsStatus())) {
                // If the CompsFilter defines Status, require the RedisProperty to match it
                status++;
                rStatuses.add(prop.getMlsStatus());
                continue;
            }

            if (filter.getPropertySubType() != null && !filter.getPropertySubType().contains(prop.getPropertySubType())) {
                subtype++;
                continue;
            }

            if (filter.getYearBuiltFrom() != null && filter.getYearBuiltTo() != null) {
                // If the CompsFilter defines both YearBuiltFrom and YearBuiltTo, require the RedisProperty to be within those bounds
                if (prop.getYearBuilt() == null || prop.getYearBuilt() < filter.getYearBuiltFrom() || prop.getYearBuilt() >= filter.getYearBuiltTo()) {
                    year++;
                    continue;
                }
            }

            if (filter.getSqftTotalFrom() != null && filter.getSqftTotalTo() != null) {
                // If the CompsFilter defines both SqftTotalFrom or SqftTotalTo, require the RedisProperty to be within those bounds
                if (prop.getSqftTotal() == null || prop.getSqftTotal() < filter.getSqftTotalFrom() || prop.getSqftTotal() >= filter.getSqftTotalTo()) {
                    sqft++;
                    continue;
                }
            }

            if (filter.getPool() != null && prop.getPoolState() != null && !filter.getPool().equals(prop.getPoolState())) {
                // If the CompsFilter defines Pool, require the RedisProperty to match it iff it also defines it
                pool++;
                continue;
            }

            if (filter.getStories() != null && prop.getStoryCount() != null && !filter.getStories().equals(prop.getStoryCount())) {
                // If the CompsFilter defines Stories, require the RedisProperty to match it iff it also defines it
                stories++;
                continue;
            }

            passedFilter.add(prop.getId());
        }

        if (log) {
            LogUtil.info("{} -> {} candidates for filter ({}, {}) {}; narrowed by: {} timestamp, {} status ({} vs {}), {} subtype, {} year, {} sqft, {} pool, {} stories",
                    byLocationNear.size(), passedFilter.size(), subjectLatitude, subjectLongitude, filter,
                    timestamp, status, filter.getStatus(), rStatuses, subtype, year, sqft, pool, stories);
        }

        if (filter.primed) {
            filter.resultsDetails = new SearchResultsRecord(byLocationNear.size(), passedFilter.size(), timestamp, status, subtype, year, sqft, pool, stories);
        }

        return passedFilter;
    }
}
