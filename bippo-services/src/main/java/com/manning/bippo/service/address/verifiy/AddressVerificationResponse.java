package com.manning.bippo.service.address.verifiy;

public interface AddressVerificationResponse {

    public String getFullAddress();
    public String getFirstLine();
    public String getLastLine();
    
    public boolean hasLatLong();
    public double getLatitude();
    public double getLongitude();
    
    public boolean hasAddressParts();
    public String getStreetNumber();
    public String getStreetName();
    public String getStreetSuffix();
    public String getCity();
    public String getState();
    public int getZipCode();
}
