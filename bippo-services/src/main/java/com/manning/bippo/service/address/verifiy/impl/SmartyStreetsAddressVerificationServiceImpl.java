package com.manning.bippo.service.address.verifiy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;
import com.manning.bippo.service.address.verifiy.pojo.SmartyStreetsStreetAddressResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("smartystreets")
@Primary // This Service will be primary for AddressVerificationService
public class SmartyStreetsAddressVerificationServiceImpl implements AddressVerificationService
{
    private static final int TIMEOUT_IN_SECONDS = 5;

    @Value("${smartystreets.auth-id}")
    String authId;

    @Value("${smartystreets.auth-token}")
    String authToken;

    CloseableHttpClient httpClient;
    ObjectMapper objectMapper;

    public SmartyStreetsAddressVerificationServiceImpl()
    {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_IN_SECONDS * 1000)
                .setConnectTimeout(TIMEOUT_IN_SECONDS * 1000)
                .build();
        (objectMapper = new ObjectMapper())
                .setTypeFactory(TypeFactory.defaultInstance()
                        .withClassLoader(SmartyStreetsAddressVerificationServiceImpl.class.getClassLoader()));
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
//
//    @Deprecated
//    public List<String> getSmartyStreetsSuggestion(String prefix, String state, int suggestions)
//    {
//        try
//        {
//            URIBuilder builder = new URIBuilder("https://autocomplete-api.smartystreets.com/suggest");
//            builder.addParameter("auth-id", authId);
//            builder.addParameter("auth-token", authToken);
//            builder.addParameter("state_filter", state);
//            builder.addParameter("prefix", prefix);
//            builder.addParameter("suggestions", Integer.toString(suggestions));
//            builder.addParameter("geolocate", Boolean.FALSE.toString());
//
//            HttpGet httpGet = new HttpGet(builder.build());
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            String responseBody = IOUtils.toString(response.getEntity().getContent());
//            LogUtil.debug("********* SmartyStreets Response *********\n" + responseBody);
//
//        } catch (URISyntaxException | IOException e)
//        {
//            throw new IllegalStateException(e.getMessage(), e);
//        }
//
//        return null;
//    }

    @Override
    public List<SmartyStreetsStreetAddressResponse> queryMultiple(Collection<String> addresses)
    {

        List<SmartyStreetsStreetAddressResponse> returnResponse;

        List<SmartyStreetsStreetRequest> requests = new ArrayList<>();
        addresses.forEach(address ->
        {
            requests.add(new SmartyStreetsStreetRequest(address));
        });

        try
        {
            URIBuilder builder = new URIBuilder("https://api.smartystreets.com/street-address");
            builder.addParameter("auth-id", authId);
            builder.addParameter("auth-token", authToken);

            HttpPost httpPost = new HttpPost(builder.build());
            BasicHttpEntity entity = new BasicHttpEntity();
            entity.setContentType("application/json");

            String streetRequests = objectMapper.writeValueAsString(requests);
            entity.setContent(IOUtils.toInputStream(streetRequests));

            httpPost.setEntity(entity);
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IllegalStateException("SmartStreets failed with error: " + responseBody);
                }
            }

            SmartyStreetsStreetAddressResponse[] smartyStreetsStreetAddressResponses = objectMapper.readValue(responseBody, SmartyStreetsStreetAddressResponse[].class);

            returnResponse = Arrays.asList(smartyStreetsStreetAddressResponses);
        } catch (URISyntaxException | IOException e)
        {
            LogUtil.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage(), e);
        }

        return returnResponse;
    }

    @Override
    public SmartyStreetsStreetAddressResponse query(String address) throws IllegalStateException
    {
        try
        {
            URIBuilder builder = new URIBuilder("https://api.smartystreets.com/street-address");
            builder.addParameter("auth-id", authId);
            builder.addParameter("auth-token", authToken);
            builder.addParameter("street", address);
            HttpGet httpGet = new HttpGet(builder.build());
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IllegalStateException("SmartStreets failed with error: " + responseBody);
                }
            }

            LogUtil.debug("********* SmartyStreets Response *********\n" + builder.build().toString() + "\n" + responseBody);

            SmartyStreetsStreetAddressResponse[] smartyStreetsStreetAddressResponses = objectMapper.readValue(responseBody, SmartyStreetsStreetAddressResponse[].class);
            if (smartyStreetsStreetAddressResponses.length == 0)
            {
                throw new IllegalStateException("No address match found for address: " + address);
            }


            return smartyStreetsStreetAddressResponses[0];

        } catch (URISyntaxException | IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public <K> Map<K, SmartyStreetsStreetAddressResponse> queryMultipleMapped(Map<K, String> propertiesThatNeedLatLong)
    {
        List<String> propertyAddresses = new ArrayList<>(propertiesThatNeedLatLong.values());
        List<SmartyStreetsStreetAddressResponse> smartStreetsStreetAddressResponse = queryMultiple(propertyAddresses);

        Map<String, K> propertiesThatNeedLatLongInverse = propertiesThatNeedLatLong.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (propertyIdLeft, propertyIdRight) -> {
                            LogUtil.debug("Duplicate key found {} ", propertyIdLeft);
                            return propertyIdRight;
                        }));

        Map<K, SmartyStreetsStreetAddressResponse> batchCordinates = new HashMap<>();
        smartStreetsStreetAddressResponse.forEach(response -> {
            String matchedAddress = propertyAddresses.get(response.getInputIndex());
            K propertyId = propertiesThatNeedLatLongInverse.get(matchedAddress);

//            Coordinates coordinates = new Coordinates();
//            coordinates.setLatitude(response.getMetadata().getLatitude().doubleValue());
//            coordinates.setLongitude(response.getMetadata().getLongitude().doubleValue());
//
            batchCordinates.put(propertyId, response);
        });
        LogUtil.debug("SmartyStreets Found Lat/Long for {} of {} properties", smartStreetsStreetAddressResponse.size(), propertiesThatNeedLatLong.size());

        return batchCordinates;
    }

    private class SmartyStreetsStreetRequest
    {
        public SmartyStreetsStreetRequest(String street)
        {
            this.street = street;
        }

        String street;
        int candidates = 1;

        public String getStreet()
        {
            return street;
        }

        public void setStreet(String street)
        {
            this.street = street;
        }

        public int getCandidates()
        {
            return candidates;
        }

        public void setCandidates(int candidates)
        {
            this.candidates = candidates;
        }
    }
}
