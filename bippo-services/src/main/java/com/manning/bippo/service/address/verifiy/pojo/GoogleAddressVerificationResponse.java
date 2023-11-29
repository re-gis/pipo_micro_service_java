package com.manning.bippo.service.address.verifiy.pojo;

import com.google.common.base.Strings;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GoogleAddressVerificationResponse implements AddressVerificationResponse {

    private static final Set<AddressComponentType> REQUIRED = new LinkedHashSet<>();
    private static final Set<AddressComponentType> DESIRED = new LinkedHashSet<>();
    
    static {
        Collections.addAll(GoogleAddressVerificationResponse.REQUIRED,
                AddressComponentType.STREET_NUMBER, AddressComponentType.ROUTE,
                AddressComponentType.LOCALITY, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1,
                AddressComponentType.POSTAL_CODE);
        Collections.addAll(GoogleAddressVerificationResponse.DESIRED,
                AddressComponentType.STREET_NUMBER, AddressComponentType.ROUTE,
                AddressComponentType.LOCALITY, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1,
                AddressComponentType.POSTAL_CODE);
    }

    public final GeocodingResult result;
    private final String line1, line2;
    private final String streetNumber, streetRoute, city, state, zip;
    private final String streetName, streetSuffix;
    private final LatLng ll;
    private final boolean hasAllParts;

    private GoogleAddressVerificationResponse(GeocodingResult result, Map<AddressComponentType, String> components) {
        // We've already verified that all of this data is present if we call this constructor
        this.result = result;
        this.ll = result.geometry.location;
        this.streetNumber = components.get(AddressComponentType.STREET_NUMBER);
        this.streetRoute = components.get(AddressComponentType.ROUTE);
        this.city = components.get(AddressComponentType.LOCALITY);
        this.state = components.get(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
        this.zip = components.get(AddressComponentType.POSTAL_CODE);

        final String fullAddress = result.formattedAddress;

        // Return format "{street number} {street name} {street suffix}"
        this.line1 = fullAddress.substring(0, fullAddress.indexOf(','));
        // Return format "{city} {state} {zip code}-{zip code suffix}" (note no commas)
        this.line2 = String.format("%s %s %s", this.city, this.state, this.zip);

        if (this.streetRoute.indexOf(' ') >= 0) {
            final int lastSpace = this.streetRoute.lastIndexOf(' ');

            this.streetName = this.streetRoute.substring(0, lastSpace);
            this.streetSuffix = this.streetRoute.substring(lastSpace + 1, this.streetRoute.length());
            this.hasAllParts = true;
        } else {
            this.streetName = this.streetSuffix = null;
            this.hasAllParts = false;
        }
    }

    public GoogleAddressVerificationResponse(GeocodingResult result) {
        this.result = result;
        this.ll = result.geometry.location;

        if (this.ll == null) {
            throw new IllegalStateException("Missing geometry.location");
        }

        final Map<AddressComponentType, String> components = new HashMap<>();
        GoogleAddressVerificationResponse.getComponents(result, components);

        if (!components.keySet().containsAll(GoogleAddressVerificationResponse.REQUIRED)) {
            final Set<AddressComponentType> missing = new HashSet<>(GoogleAddressVerificationResponse.REQUIRED);
            missing.removeAll(components.keySet());

            throw new IllegalStateException("Missing or incomplete addressComponents: " + missing);
        }

        this.streetNumber = components.get(AddressComponentType.STREET_NUMBER);
        this.streetRoute = components.get(AddressComponentType.ROUTE);
        this.city = components.get(AddressComponentType.LOCALITY);
        this.state = components.get(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
        this.zip = components.get(AddressComponentType.POSTAL_CODE);

        final String fullAddress = result.formattedAddress;

        if (fullAddress == null || fullAddress.indexOf(',') < 0) {
            throw new IllegalStateException("Missing or incomplete formattedAddress");
        }

        // Return format "{street number} {street name} {street suffix}"
        this.line1 = fullAddress.substring(0, fullAddress.indexOf(','));
        // Return format "{city} {state} {zip code}-{zip code suffix}" (note no commas)
        this.line2 = String.format("%s %s %s", this.city, this.state, this.zip);

        if (this.streetRoute.indexOf(' ') >= 0) {
            final int lastSpace = this.streetRoute.lastIndexOf(' ');

            this.streetName = this.streetRoute.substring(0, lastSpace);
            this.streetSuffix = this.streetRoute.substring(lastSpace + 1, this.streetRoute.length());
            this.hasAllParts = true;
        } else {
            this.streetName = this.streetSuffix = null;
            this.hasAllParts = false;
        }
    }

    @Override
    public String getFullAddress() {
        return this.result.formattedAddress;
    }

    @Override
    public String getFirstLine() {
        return this.line1;
    }

    @Override
    public String getLastLine() {
        return this.line2;
    }

    @Override
    public boolean hasLatLong() {
        return true;
    }

    @Override
    public double getLatitude() {
        return this.ll.lat;
    }

    @Override
    public double getLongitude() {
        return this.ll.lng;
    }

    @Override
    public boolean hasAddressParts() {
        return this.hasAllParts;
    }

    @Override
    public String getStreetNumber() {
        return this.streetNumber;
    }

    public String getStreetRoute() {
        return this.streetRoute;
    }

    @Override
    public String getStreetName() {
        return this.streetName;
    }

    @Override
    public String getStreetSuffix() {
        return this.streetSuffix;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public int getZipCode() {
        return Integer.parseInt(this.zip);
    }


    public static GoogleAddressVerificationResponse create(GeocodingResult result, Object context, String address, OnBoardInformaticsPropertyService onboard) {
        if (result.geometry.location == null) {
            throw new IllegalStateException("Missing geometry.location");
        }

        if (result.formattedAddress == null || result.formattedAddress.indexOf(',') < 0) {
            throw new IllegalStateException("Missing or incomplete formattedAddress");
        }

        final Map<AddressComponentType, String> components = new HashMap<>();
        GoogleAddressVerificationResponse.getComponents(result, components);
        System.out.println(components);

        if (!components.keySet().containsAll(GoogleAddressVerificationResponse.REQUIRED)) {
            final Set<AddressComponentType> missing = new HashSet<>(GoogleAddressVerificationResponse.REQUIRED);
            missing.removeAll(components.keySet());

            throw new IllegalStateException("Missing or incomplete addressComponents: " + missing);
        }

        if (!components.containsKey(AddressComponentType.POSTAL_CODE_SUFFIX)) {
            // This will set the postal code suffix if it returns successfully, otherwise it will throw
            GoogleAddressVerificationResponse.fixPostalCodeSuffix(onboard, context, address, components);
        }

        return new GoogleAddressVerificationResponse(result, components);
    }

    private static void fixPostalCodeSuffix(OnBoardInformaticsPropertyService onboard, Object context, String fullAddress, Map<AddressComponentType, String> components) {
        if (context instanceof NtreisProperty) {
            final String plus4 = ((NtreisProperty) context).getPostalCodePlus4();

            if (plus4 != null && plus4.length() == 4 && plus4.replaceAll("\\D", "").length() == 4) {
                components.put(AddressComponentType.POSTAL_CODE_SUFFIX, plus4);
                LogUtil.info("Repaired POSTAL_CODE_SUFFIX using NtreisProperty: " + fullAddress);
                return;
            }
        }

        // Google's Geocoding API does not always supply us with the postal code suffix
        // This is currently requried by AddressSemantics, so we refer to onboard to get the postal code info when necessary
        final AllEventsPropertyResponse resp;

        if (context instanceof NtreisProperty) {
            resp = onboard.findByNtreisProperty((NtreisProperty) context, false);
        } else {
            // Only handle NtreisProperties for the time being, as they are subject to caching
            throw new IllegalStateException("Missing POSTAL_CODE_SUFFIX!");
//            final String firstLine = fullAddress.substring(0, fullAddress.indexOf(','));
//            final String lastLine = String.format("%s %s %s", components.get(AddressComponentType.LOCALITY),
//                    components.get(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1), components.get(AddressComponentType.POSTAL_CODE));
//            resp = onboard.getAllEvents(firstLine, lastLine);
        }

        if (resp != null) {
            for (AllEventsPropertyResponse.Property p : resp.property) {
                final String suffix;

                if (p != null && p.address != null && (suffix = p.address.postal2) != null && !suffix.isEmpty()) {
                    components.put(AddressComponentType.POSTAL_CODE_SUFFIX, suffix);

                    if (context instanceof NtreisProperty) {
                        LogUtil.info("Repaired POSTAL_CODE_SUFFIX using onboard, for NtreisProperty: " + fullAddress);
                    } else {
                        LogUtil.info("Repaired POSTAL_CODE_SUFFIX using onboard, for " + fullAddress);
                    }

                    return;
                }
            }
        }

        throw new IllegalStateException("Missing POSTAL_CODE_SUFFIX and was unable to locate it from onboard.");
    }

    private static void getComponents(GeocodingResult result, Map<AddressComponentType, String> components) {
        final Set<AddressComponentType> currentTypes = new HashSet<>();

        for (AddressComponent ac : result.addressComponents) {
            if (Strings.isNullOrEmpty(ac.shortName)) {
                // Cause any null/empty values to be absent entirely
                continue;
            }

            for (AddressComponentType type : ac.types) {
                // We currently always prefer the shortName, but if this ever changes we'll need to configure that alongside REQUIRED_COMPONENTS
                components.put(type, ac.shortName);
            }

            currentTypes.clear();
        }
    }
}
