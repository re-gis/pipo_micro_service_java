package com.manning.bippo.service.address.verifiy.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartyStreetsStreetAddressResponse implements AddressVerificationResponse
{
    SmartyStreetsMetadata metadata;
    SmartyStreetsComponents components;

    @JsonProperty("input_index")
    int inputIndex;

    @JsonProperty("candidate_index")
    int candidateIndex;

    @JsonProperty("delivery_line_1")
    String deliveryLine1;

    @JsonProperty("last_line")
    String lastLine;

    private transient String lastLineTrimmed = null;

    public SmartyStreetsMetadata getMetadata()
    {
        return metadata;
    }

    public void setMetadata(SmartyStreetsMetadata metadata)
    {
        this.metadata = metadata;
    }

    public SmartyStreetsComponents getComponents()
    {
        return components;
    }

    public void setComponents(SmartyStreetsComponents components)
    {
        this.components = components;
    }

    public int getInputIndex()
    {
        return inputIndex;
    }

    public void setInputIndex(int inputIndex)
    {
        this.inputIndex = inputIndex;
    }

    public int getCandidateIndex()
    {
        return candidateIndex;
    }

    public void setCandidateIndex(int candidateIndex)
    {
        this.candidateIndex = candidateIndex;
    }

    public String getDeliveryLine1()
    {
        return deliveryLine1;
    }

    public void setDeliveryLine1(String deliveryLine1)
    {
        this.deliveryLine1 = deliveryLine1;
    }

    public void setLastLine(String lastLine)
    {
        this.lastLineTrimmed = null;
        this.lastLine = lastLine;
    }

    @Override
    public String getFullAddress() {
        return String.format("%s, %s", this.getFirstLine(), this.getLastLine());
    }

    @Override
    public String getFirstLine() {
        return this.deliveryLine1;
    }

    @Override
    public String getLastLine() {
        final String last;

        if (this.lastLineTrimmed == null && (last = this.lastLine) != null) {
            this.lastLineTrimmed = last.indexOf('-') >= 0 ? last.substring(0, last.lastIndexOf('-')) : last;
        }

        return this.lastLineTrimmed;
    }

    @Override
    public boolean hasLatLong() {
        return this.metadata.latitude != null && this.metadata.longitude != null;
    }

    @Override
    public double getLatitude() {
        return this.metadata.latitude.doubleValue();
    }

    @Override
    public double getLongitude() {
        return this.metadata.longitude.doubleValue();
    }

    @Override
    public boolean hasAddressParts() {
        return this.components != null;
    }

    @Override
    public String getStreetNumber() {
        return this.components.primaryNumber;
    }

    @Override
    public String getStreetName() {
        return this.components.streetName;
    }

    @Override
    public String getStreetSuffix() {
        return this.components.streetSuffix;
    }

    @Override
    public String getCity() {
        return this.components.cityName;
    }

    @Override
    public String getState() {
        return this.components.stateAbbreviation;
    }

    @Override
    public int getZipCode() {
        return this.components.zipcode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class SmartyStreetsMetadata
    {
        BigDecimal latitude;
        BigDecimal longitude;

        @JsonProperty("county_name")
        String countyName;

        public BigDecimal getLatitude()
        {
            return latitude;
        }

        public void setLatitude(BigDecimal latitude)
        {
            this.latitude = latitude;
        }

        public BigDecimal getLongitude()
        {
            return longitude;
        }

        public void setLongitude(BigDecimal longitude)
        {
            this.longitude = longitude;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class SmartyStreetsComponents
    {
        @JsonProperty("primary_number")
        String primaryNumber;

        @JsonProperty("street_name")
        String streetName;

        @JsonProperty("street_suffix")
        String streetSuffix;

        @JsonProperty("city_name")
        String cityName;

        @JsonProperty("default_city_name")
        String defaultCityName;

        @JsonProperty("state_abbreviation")
        String stateAbbreviation;

        @JsonProperty("zipcode")
        Integer zipcode;

        @JsonProperty("plus4_code")
        Integer plus4Code;


        public String getPrimaryNumber()
        {
            return primaryNumber;
        }

        public void setPrimaryNumber(String primaryNumber)
        {
            this.primaryNumber = primaryNumber;
        }

        public String getStreetName()
        {
            return streetName;
        }

        public void setStreetName(String streetName)
        {
            this.streetName = streetName;
        }

        public String getStreetSuffix()
        {
            return streetSuffix;
        }

        public void setStreetSuffix(String streetSuffix)
        {
            this.streetSuffix = streetSuffix;
        }

        public String getCityName()
        {
            return cityName;
        }

        public void setCityName(String cityName)
        {
            this.cityName = cityName;
        }

        public String getDefaultCityName()
        {
            return defaultCityName;
        }

        public void setDefaultCityName(String defaultCityName)
        {
            this.defaultCityName = defaultCityName;
        }

        public String getStateAbbreviation()
        {
            return stateAbbreviation;
        }

        public void setStateAbbreviation(String stateAbbreviation)
        {
            this.stateAbbreviation = stateAbbreviation;
        }

        public Integer getZipcode()
        {
            return zipcode;
        }

        public void setZipcode(Integer zipcode)
        {
            this.zipcode = zipcode;
        }

        public Integer getPlus4Code()
        {
            return plus4Code;
        }

        public void setPlus4Code(Integer plus4Code)
        {
            this.plus4Code = plus4Code;
        }
    }
}