package com.manning.bippo.service.zillow;

import com.manning.bippo.dao.pojo.ZillowProperty;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;

public class ZillowAddressVerificiationResponse implements AddressVerificationResponse {

    final String firstLine, lastLine, city, state, zipCode;
    final double latitude, longitude;

    public ZillowAddressVerificiationResponse(ZillowProperty zillowProperty) {
        this.firstLine = zillowProperty.getFirstLine();
        this.city = zillowProperty.getCity();
        this.state = zillowProperty.getState();
        this.latitude = zillowProperty.getLatitude();
        this.longitude = zillowProperty.getLongitude();

        final String zip;

        if ((zip = zillowProperty.getZipCode()).indexOf('-') >= 0) {
            this.zipCode = zip.substring(0, zip.lastIndexOf('-'));
        } else {
            this.zipCode = zip;
        }

        this.lastLine = String.format("%s %s %s", this.city, this.state, this.zipCode);
    }

    @Override
    public String getFullAddress() {
        return String.format("%s, %s", this.firstLine, this.lastLine);
    }

    @Override
    public String getFirstLine() {
        return this.firstLine;
    }

    @Override
    public String getLastLine() {
        return this.lastLine;
    }

    @Override
    public boolean hasLatLong() {
        return true;
    }

    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public boolean hasAddressParts() {
        return false;
    }

    @Override
    public String getStreetNumber() {
        throw new UnsupportedOperationException("Not supported by address verification through Zillow.");
    }

    @Override
    public String getStreetName() {
        throw new UnsupportedOperationException("Not supported by address verification through Zillow.");
    }

    @Override
    public String getStreetSuffix() {
        throw new UnsupportedOperationException("Not supported by address verification through Zillow.");
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
        return Integer.parseInt(this.zipCode);
    }
}
