package com.manning.bippo.service.area;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.MLSStatusRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.MLSStatus;
import com.manning.bippo.service.redis.RedisPropertyService;
import com.manning.bippo.service.rets.pojo.CompsResponse;
import com.manning.bippo.service.trestle.TrestleDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AreaListingsServiceImpl implements AreaListingsService
{
    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;
    
    @Autowired
    MLSStatusRepository mlsStatusRepository;

    @Autowired
    RedisPropertyService redisPropertyService;

//    @Deprecated
//    public CompsResponse getAreaListings(NtreisProperty ntreisProperty, Float distance)
//    {
//        if (ntreisProperty != null) {
//            if (ntreisProperty.getLongitude() != null && ntreisProperty.getLatitude() != null) {
//                final List<String> validStatuses = this.mlsStatusRepository.findByInvalidOrderByValueAsc(false).stream().map(s -> s.getValue()).collect(Collectors.toList());
//                validStatuses.add("Sold");
//                List<NtreisProperty> areas = ntreisPropertyRepository.findByDistanceAndRecency(distance.doubleValue(), DateUtils.addDays(new Date(), -180),
//                        validStatuses, ntreisProperty.getLongitude(), ntreisProperty.getLatitude());
//
//                return new CompsResponse(null, areas);
//            } else {
//
//                LogUtil.debug("No Lat/Long co-ordinates found for address: {}", ntreisProperty.getAddress());
//            }
//        }
//        return new CompsResponse(null, Collections.emptyList());
//    }

    public CompsResponse getAreaListings(BasicPropertyDetails propertyDetails, Float radius)
    {
        final CompsFilterCalculated filter = new CompsFilterCalculated();
        filter.proximityInMiles = radius.doubleValue();
        filter.statusChangeInDays = 180;
        filter.setStatus(null);

        final String region = propertyDetails.getMlsRegion();

        if (region != null) {
            final String subtype;

            switch (region) {
                case "NTREIS":
                    // If subject is not Residential, default to Residential/RES-Single Family
                    subtype = "Residential".equalsIgnoreCase(propertyDetails.getMlsPropertyType()) ? propertyDetails.getMlsPropertySubType() : "RES-Single Family";
                    break;
                default:
                    // Just default to Residential/null due to the way Trestle data works
                    subtype = null;
                    break;
            }

            filter.setPropertyType("Residential");
            filter.setPropertySubType(subtype == null ? null : Collections.singletonList(subtype));
        }

        final List<? extends AbstractProperty> listingsByRedis = redisPropertyService.findByFilterInfer(
                propertyDetails.getMlsRegion(), propertyDetails.getLongitude(), propertyDetails.getLatitude(), filter);
        final List<String> validStatuses = this.getAcceptedStatuses(region);

        for (Iterator<? extends AbstractProperty> it = listingsByRedis.iterator(); it.hasNext(); ) {
            final AbstractProperty prop = it.next();

            // Remove invalid statuses; we also need to filter by property type as the redis search does not support it
            if (!"Residential".equals(prop.getPropertyType()) || !validStatuses.contains(prop.getStatus())) {
                it.remove();
            }
        }

        LogUtil.debug("Found {} Listings near {}", listingsByRedis.size(), propertyDetails.getAddress());

        return new CompsResponse(filter, listingsByRedis);
    }

    private List<String> getAcceptedStatuses(String region) {
        final List<String> validStatuses = mlsStatusRepository.findByInvalidOrderByValueAsc(false).stream().map(MLSStatus::getValue).collect(Collectors.toList());
        validStatuses.add("Sold");

        if (region != null && !"NTREIS".equalsIgnoreCase(region)) {
            for (ListIterator<String> it = validStatuses.listIterator(); it.hasNext(); ) {
                it.set(TrestleDataUtils.mapNtreisStatusToTrestle(it.next()));
            }
        }

        return validStatuses;
    }
}
