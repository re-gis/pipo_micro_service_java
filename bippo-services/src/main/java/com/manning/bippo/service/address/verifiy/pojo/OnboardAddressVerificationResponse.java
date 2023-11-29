package com.manning.bippo.service.address.verifiy.pojo;

import com.google.common.primitives.Doubles;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;

public class OnboardAddressVerificationResponse implements AddressVerificationResponse {
    
    public final AllEventsPropertyResponse.Property property;
    private final double latitude;
    private final double longitude;
    private final boolean hasLatLong;

    public OnboardAddressVerificationResponse(AllEventsPropertyResponse.Property property) {
        this.property = property;

        if (property.location != null && property.location.latitude != null && property.location.longitude != null) {
            final Double lat = Doubles.tryParse(property.location.latitude), lon = Doubles.tryParse(property.location.longitude);

            if (lat != null && lon != null) {
                this.latitude = lat;
                this.longitude = lon;
                this.hasLatLong = true;
                return;
            }
        }
        this.latitude = this.longitude = Float.NaN;
        this.hasLatLong = false;
    }

    @Override
    public String getFullAddress() {
        return this.property != null && this.property.address != null ? this.property.address.oneLine : null;
    }

    @Override
    public String getFirstLine() {
        return this.property != null && this.property.address != null ? this.property.address.line1 : null;
    }

    @Override
    public String getLastLine() {
        if (this.property != null && this.property.address != null) {
            return this.property.address.line2.replaceAll(",", "");
        }

        return null;
    }

    @Override
    public boolean hasLatLong() {
        return this.hasLatLong;
    }

    @Override
    public double getLatitude() {
        if (this.hasLatLong) {
            return this.latitude;
        }

        throw new UnsupportedOperationException("Lat/Long not present.");
    }

    @Override
    public double getLongitude() {
        if (this.hasLatLong) {
            return this.longitude;
        }

        throw new UnsupportedOperationException("Lat/Long not present.");
    }

    @Override
    public boolean hasAddressParts() {
        // We cannot implement street number, name, and suffix without messy and error-prone guesswork involved
        return false;
    }

    @Override
    public String getStreetNumber() {
        throw new UnsupportedOperationException("OnboardAddressVerificationResponses cannot support street number/name/suffix.");
    }

    @Override
    public String getStreetName() {
        throw new UnsupportedOperationException("OnboardAddressVerificationResponses cannot support street number/name/suffix.");
    }

    @Override
    public String getStreetSuffix() {
        throw new UnsupportedOperationException("OnboardAddressVerificationResponses cannot support street number/name/suffix.");
    }

    @Override
    public String getCity() {
        return this.property != null && this.property.address != null ? this.property.address.locality : null;
    }

    @Override
    public String getState() {
        return this.property != null && this.property.address != null ? this.property.address.countrySubd : null;
    }

    @Override
    public int getZipCode() {
        return this.property != null && this.property.address != null ? Integer.parseInt(this.property.address.postal1) : 0;
    }
}
