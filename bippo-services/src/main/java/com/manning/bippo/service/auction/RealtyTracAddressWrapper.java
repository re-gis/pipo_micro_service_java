package com.manning.bippo.service.auction;

import com.manning.bippo.dao.pojo.RealtyTracAuction;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;

final class RealtyTracAddressWrapper implements AddressVerificationResponse {

    final String firstLine;
    final String lastLine;

    public RealtyTracAddressWrapper(RealtyTracAuction auc) {
        this.firstLine = auc.getFirstLine();
        this.lastLine = auc.getLastLine();
    }

    @Override
    public String getFullAddress() {
        return this.firstLine + ", " + this.lastLine;
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
        return false;
    }

    @Override
    public double getLatitude() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public double getLongitude() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public boolean hasAddressParts() {
        return false;
    }

    @Override
    public String getStreetNumber() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public String getStreetName() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public String getStreetSuffix() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public String getCity() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public String getState() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }

    @Override
    public int getZipCode() {
        throw new UnsupportedOperationException("Unsupported by RealtyTracAddressWrapper.");
    }
    
}
