package com.manning.bippo.service.geocode.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.manning.bippo.service.geocode.GeocodeService;
import com.manning.bippo.service.geocode.pojo.AddressMatch;
import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import com.manning.bippo.dao.itf.Addressable;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address.verifiy.pojo.GoogleAddressVerificationResponse;
import com.manning.bippo.service.geocode.pojo.GeocodeResponse;
import com.manning.bippo.service.geocode.pojo.Result;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("google")
@Primary // This Service will be primary for Autowiring GeocodeService
public class GoogleGeocodeServiceImpl implements GeocodeService//, AddressVerificationService
{
    GeoApiContext context;

    @Autowired
    OnBoardInformaticsPropertyService onboardService;

    public GoogleGeocodeServiceImpl()
    {
        // TODO: Extract API key to configuration file if it needs to be separated between dev/prod?
        context = new GeoApiContext().setApiKey("AIzaSyAsO0x7MOc32nIPP8z7ykY3cRK4HdR5Y9M");
    }

    @Override
    public GeocodeResponse geocodeOneLineAddress(String address)
    {
        GeocodeResponse response = new GeocodeResponse();
        Result result = new Result();

        try
        {
            GeocodingResult[] geocode = GeocodingApi.geocode(context, address).await();

            for (GeocodingResult geocodingResult : geocode)
            {
                AddressMatch match = new AddressMatch();
                LatLongCoordinates coordinates = new LatLongCoordinates();
                coordinates.setLatitude(geocodingResult.geometry.location.lat);
                coordinates.setLongitude(geocodingResult.geometry.location.lng);
                match.setCoordinates(coordinates);
                result.getAddressMatches().add(match);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        response.setResult(result);
        return response;
    }

//    @Override
    public AddressVerificationResponse query(Addressable addressable) throws IOException {
        return this.query(addressable.getAddress(), addressable);
    }

//    @Override
    public GoogleAddressVerificationResponse query(String address) throws IOException {
        return this.query(address, null);
    }

    private GoogleAddressVerificationResponse query(String address, Addressable ctx) throws IOException {
        final GeocodingResult[] results;
        
        try {
            results = GeocodingApi.geocode(this.context, address).await();
        } catch (Exception e) {
            throw new IOException("Failed to request Google Geocoding API.", e);
        }

        if (results == null || results.length < 1) {
            throw new IOException("Missing or incomplete response from Google Geocoding API.");
        }

        try {
            return GoogleAddressVerificationResponse.create(results[0], ctx, address, this.onboardService);
        } catch (IllegalStateException e) {
            throw new IOException("Missing or incomplete response from Google Geocoding API.", e);
        }
    }

//    @Override
    public List<GoogleAddressVerificationResponse> queryMultiple(Collection<String> addresses) {
        final List<GoogleAddressVerificationResponse> responses = new ArrayList<>();

        for (String address : addresses) {
            try {
                responses.add(this.query(address));
            } catch (IOException e) {
                throw new IllegalStateException("Query failed.", e);
            }
        }

        return responses;
    }

//    @Override
    public <K> Map<K, GoogleAddressVerificationResponse> queryMultipleMapped(Map<K, String> properties) {
        final Map<K, GoogleAddressVerificationResponse> responses = new HashMap<>();

        for (Map.Entry<K, String> entry : properties.entrySet()) {
            try {
                responses.put(entry.getKey(), this.query(entry.getValue()));
            } catch (IOException | IllegalStateException e) {
                // Error, silently continue
            }
        }

        return responses;
    }
}
