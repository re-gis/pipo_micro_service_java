package com.manning.bippo.service.property;

import com.google.common.base.Strings;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.OnBoardInformaticsResponse;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.geocode.RooftopLatLongService;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import com.manning.bippo.service.trestle.TrestleDataService;
import com.manning.bippo.service.utils.BippoUtils;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicPropertyBuilderService
{

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    TrestleDataService trestleDataService;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    @Autowired
    OnBoardInformaticsPropertyService onBoardInformaticsPropertyService;

    @Autowired
    RooftopLatLongService latLongService;

    public BasicPropertyDetails build(String bippoId, boolean canBlock)
    {
        final String mlsNumber = BippoUtils.getMLSNumber(bippoId);

        if (mlsNumber != null) {
            return build(ntreisPropertyRepository.findByMLSNumber(Integer.parseInt(mlsNumber)), true);
        }

        final String trestleId = BippoUtils.getTrestleId(bippoId);

        if (trestleId != null) {
            final String[] split = trestleId.split("_");

            if (split.length > 1) {
                return build(this.trestleDataService.findByRegionAndListingKey(split[0], Integer.parseInt(split[1])), true);
            }
        }

        final String addressSemanticsPropId = BippoUtils.getAddressSemanticsPropId(bippoId);

        if (addressSemanticsPropId != null) {
            return build(addressSemanticizationService.findOne(Long.valueOf(addressSemanticsPropId)), canBlock);
        }

        final String onboardId = BippoUtils.getOnBoardId(bippoId);

        if (onboardId != null) {
            return build(onBoardInformaticsPropertyService.findById(onboardId));
        }

        throw new IllegalStateException("Appropriate BIPPO ID was not passed: " + bippoId);
    }

    public BasicPropertyDetails build(OnBoardInformaticsResponse byId)
    {
        AllEventsPropertyResponse allEventsPropertyResponse = onBoardInformaticsPropertyService.convertJSONToAllEventsResponse(byId.getResponse());

        switch (allEventsPropertyResponse.property.length) {
            case 0:
                throw new IllegalStateException("No available data for OnBoardInformaticsResponse id " + byId.getId());
            case 1:
                break;
            default:
                LogUtil.warn("AllEventsPropertyResponse has " + allEventsPropertyResponse.property.length + " distinct responses");
                break;
        }

        return build(allEventsPropertyResponse);
    }

    public BasicPropertyDetails build(AddressSemantics one, boolean canBlock)
    {
        if (one.getNtreisProperty() != null) {
            // false because there is obviously no need to force, as we have the AddressSemantics
            return build(one.getNtreisProperty(), false);
        }

        if (one.getTrestleId() != null && StringUtils.isNotBlank(one.getTrestleSystem())) {
            return build(trestleDataService.findByRegionAndId(one.getTrestleSystem(), one.getTrestleId()), false);
        }

        AllEventsPropertyResponse allEventsPropertyResponse = null;

        if (one.getTaxId() == null || one.getTaxId().getResponse() == null) {
            if (canBlock) {
                final OnBoardInformaticsResponse ob = this.onBoardInformaticsPropertyService.getOnBoardInformaticsResponse(one.getFirstLine(), one.getLastLine());

                if (ob != null) {
                    one.setTaxId(ob);
                    one = this.addressSemanticsRepository.save(one);
                    allEventsPropertyResponse = this.onBoardInformaticsPropertyService.convertJSONToAllEventsResponse(ob.getResponse());
                }
            }
        } else {
            allEventsPropertyResponse = onBoardInformaticsPropertyService.convertJSONToAllEventsResponse(one.getTaxId().getResponse());
        }

        if (allEventsPropertyResponse == null || allEventsPropertyResponse.property == null || allEventsPropertyResponse.property.length < 1) {
            throw new IllegalStateException("No available tax data for " + one.getFirstLine() + ", " + one.getLastLine());
        } else if (allEventsPropertyResponse.property.length > 1) {
            LogUtil.warn("AllEventsPropertyResponse has " + allEventsPropertyResponse.property.length + "x for " + one.getFirstLine() + ", " + one.getLastLine());
        }

        BasicPropertyDetails build = build(allEventsPropertyResponse);
        build.setAddressSemanticsId(one.getId());

        try {
            LatLongCoordinates ll = this.latLongService.getLatLong(one);
            build.setLatitude(ll.getLatitude().floatValue());
            build.setLongitude(ll.getLongitude().floatValue());
        } catch (Exception e) {
            // Couldn't find rooftop quality lat/long
        }

        return build;
    }

    public BasicPropertyDetails build(AllEventsPropertyResponse allEventsPropertyResponse)
    {
        AllEventsPropertyResponse.Property property = allEventsPropertyResponse.property[0];
        int spaces = property.building.parking.prkgSpaces == null ? 0 : Integer.parseInt(property.building.parking.prkgSpaces);

        if (spaces < 0 || spaces > 4) {
            spaces = 0;
        }

        int garage = spaces;

        if (garage < 1 && property.building.parking.prkgSize > 0L) {
            garage = Math.max(1, (int) (property.building.parking.prkgSize / 199L));
        }

        return BasicPropertyDetails.builder().addressLine1(property.address.line1)
                .addressLine2(property.address.line2)
                .latitude(Float.valueOf(property.location.latitude))
                .longitude(Float.valueOf(property.location.longitude))
                .livingSize((float) property.building.size.livingSize)
                .postalCode(property.address.postal1)
                .apn(property.identifier.apn)
                .propertyType(property.summary.propType)
                .propertySubType(property.summary.propSubType)
                .mlsStatus(null)
                .mlsPropertyType(convertToMLSPropertyType(property.summary.propType))
                .mlsPropertySubType(convertToMLSPropertySubType(property.summary.propSubType))
                .yearBuiltEffective((int) property.summary.yearBuilt)
                .bathsFull((int) property.building.rooms.bathsFull)
                .bathsHalf((int) property.building.rooms.bathsHalf)
                .bedsTotal((int) property.building.rooms.beds)
                .parkingSpacesCarport(spaces) // Why are we doing this? What are the correct fields for carport and garage?
                .parkingSpacesGarage(garage)
                .pool(BasicPropertyBuilderService.mapPoolInd(property.lot.poolInd))
                .listPrice((float) property.sale.amount.saleAmt)
                .conflictingTaxData(allEventsPropertyResponse.property.length > 1)
                .mlsRegion(inferMlsRegion(property.address.countrySubd, property.area.countrysecsubd))
                .build();
    }

    /**
     * TODO: FIX ME to give the correct mls property subtype
     */
    private String convertToMLSPropertySubType(String propSubType)
    {
        return "RES-Single Family";
    }

    /**
     * TODO: FIX ME to give the correct mls property type
     */
    private String convertToMLSPropertyType(String propType)
    {
        return "Residential";
    }

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    public BasicPropertyDetails build(NtreisProperty ntreisProperty, boolean forceSemantics)
    {
        if (ntreisProperty.getLatitude() == null || ntreisProperty.getLongitude() == null) {
            if ((ntreisProperty = this.latLongService.propagateLatLong(ntreisProperty)) == null) {
                throw new IllegalStateException("No Lat/Long co-ordinates found for address (and failed to propagate): " + ntreisProperty.getAddress());
            }
        }

        final AddressSemantics byNtreisProperty;

        if (forceSemantics) {
            byNtreisProperty = this.addressSemanticizationService.indexIfAbsent(ntreisProperty);
        } else {
            byNtreisProperty = addressSemanticsRepository.findTopByNtreisProperty(ntreisProperty);
        }

        return BasicPropertyDetails.builder().addressLine1(ntreisProperty.getAddressLine1())
                .addressLine2(ntreisProperty.getAddressLine2())
                .latitude(ntreisProperty.getLatitude())
                .longitude(ntreisProperty.getLongitude())
                .livingSize(ntreisProperty.getSqFtTotal())
                .postalCode(ntreisProperty.getPostalCode())
                .apn(ntreisProperty.getParcelNumber())
                .propertyType(ntreisProperty.getPropertyType())
                .propertySubType(ntreisProperty.getPropertySubType())
                .mlsStatus(ntreisProperty.getStatus())
                .mlsPropertyType(ntreisProperty.getPropertyType())
                .mlsPropertySubType(ntreisProperty.getPropertySubType())
                .yearBuiltEffective(ntreisProperty.getYearBuilt())
                .addressSemanticsId(byNtreisProperty == null ? null : byNtreisProperty.getId())
                .bathsFull(ntreisProperty.getBathsFull())
                .bathsHalf(ntreisProperty.getBathsHalf())
                .bedsTotal(ntreisProperty.getBedsTotal())
                .parkingSpacesCarport(ntreisProperty.getParkingSpacesCarport())
                .parkingSpacesGarage(ntreisProperty.getParkingSpacesGarage())
                .pool(BasicPropertyBuilderService.mapPoolInd(ntreisProperty.getPoolYN()))
                .listPrice(ntreisProperty.getListPrice())
                .mlsRegion("NTREIS")
                .build();
    }

    public BasicPropertyDetails build(TrestleProperty trestleProperty, boolean forceSemantics)
    {
        if (trestleProperty.getLatitude() == null || trestleProperty.getLongitude() == null) {
            throw new IllegalStateException("No Lat/Long co-ordinates found for TrestleProperty (attempted prop. NYI)");
        }

        return BasicPropertyDetails.builder().addressLine1(trestleProperty.getAddressLine1())
                .addressLine2(trestleProperty.getAddressLine2())
                .latitude(trestleProperty.getLatitude() == null ? null : trestleProperty.getLatitude().floatValue())
                .longitude(trestleProperty.getLongitude() == null ? null : trestleProperty.getLongitude().floatValue())
                .livingSize(trestleProperty.getLivingArea() == null ? null : trestleProperty.getLivingArea().floatValue())
                .postalCode(trestleProperty.getPostalCode())
                .apn(trestleProperty.getParcelNumber())
                .propertyType(trestleProperty.getPropertyType())
                .propertySubType(trestleProperty.getPropertySubType())
                .mlsStatus(trestleProperty.getStandardStatus())
                .mlsPropertyType(trestleProperty.getPropertyType())
                .mlsPropertySubType(trestleProperty.getPropertySubType())
                .yearBuiltEffective(trestleProperty.getYearBuilt())
                .addressSemanticsId(null) // TODO Link AddressSemantics data to BasicPropertyDetails created from TrestleProperties
                .bathsFull(trestleProperty.getBathroomsFull())
                .bathsHalf(trestleProperty.getBathroomsHalf())
                .bedsTotal(trestleProperty.getBedroomsTotal())
                .parkingSpacesCarport(trestleProperty.getCarportSpaces() == null ? null : trestleProperty.getCarportSpaces().intValue())
                .parkingSpacesGarage(trestleProperty.getGarageSpaces() == null ? null : trestleProperty.getGarageSpaces().intValue())
                .pool(BasicPropertyBuilderService.mapPoolInd(trestleProperty.getPoolFeatures()))
                .listPrice(trestleProperty.getListPrice() == null ? null : trestleProperty.getListPrice().floatValue())
                .mlsRegion(trestleProperty.getOriginatingSystemName())
                .id(trestleProperty.getId())
                .build();
    }


    public static Boolean mapPoolInd(String pool) {
        if (pool == null || pool.trim().isEmpty()) {
            return null;
        } else if (pool.length() > 1) {
            return Boolean.TRUE;
        } else switch (pool.toLowerCase()) {
            case "1":
            case "y":
                return Boolean.TRUE;
            case "0":
            case "n":
            default:
                return Boolean.FALSE;
        }
    }

    private static final Map<String, Map<String, String>> regionsByState = new HashMap<>();
    private static final Map<String, String> regionByCountyTexas = new HashMap<>();

    static {
        // TODO Integrate this into the database and load it from there instead
        regionByCountyTexas.put("Harris", "HAR");
        regionByCountyTexas.put("Fort Bend", "HAR");
        regionByCountyTexas.put("Montgomery", "HAR");
        regionByCountyTexas.put("Galveston", "HAR");
        regionByCountyTexas.put("Brazoria", "HAR");
        regionByCountyTexas.put("Chambers", "HAR");
        regionByCountyTexas.put("Walker", "HAR");
        regionsByState.put("TX", regionByCountyTexas);
    }

    public static String inferMlsRegion(String state, String county) {
        if (county != null && county.endsWith(" County")) {
            county = county.substring(0, county.length() - 7);
        }

        final Map<String, String> regions = regionsByState.get(state);
        return regions == null ? null : regions.get(county);
    }
}
