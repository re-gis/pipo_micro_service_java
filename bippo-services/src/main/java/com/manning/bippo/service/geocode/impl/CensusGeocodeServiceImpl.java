package com.manning.bippo.service.geocode.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.geocode.GeocodeService;
import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import com.manning.bippo.service.geocode.pojo.GeocodeResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CensusGeocodeServiceImpl implements GeocodeService
{
    private static final int TIMEOUT_IN_SECONDS = 5;

    ObjectMapper objectMapper;
    CloseableHttpClient httpClient;

    public CensusGeocodeServiceImpl()
    {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_IN_SECONDS * 1000)
                .setConnectTimeout(TIMEOUT_IN_SECONDS * 1000)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public Map<Long, LatLongCoordinates> batchGeocoding(Map<Long, String> matrixUniqueIdAddressMap)
    {
        File scratchFile = null;
        try
        {
            scratchFile = File.createTempFile(RandomStringUtils.randomAlphabetic(7), ".csv");
            List<String> lines = new ArrayList<>();
            for (Map.Entry<Long, String> matrixuniqueIdAddress : matrixUniqueIdAddressMap.entrySet())
            {
                lines.add(matrixuniqueIdAddress.getKey() + "," + matrixuniqueIdAddress.getValue());
            }
            FileUtils.writeLines(scratchFile, lines);

            URIBuilder builder = new URIBuilder("https://geocoding.geo.census.gov/geocoder/locations/addressbatch");
            builder.addParameter("benchmark", "9");
            builder.addParameter("format", "json");

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            FormBodyPartBuilder formBodyPartBuilder = FormBodyPartBuilder.create("addressFile", new FileBody(scratchFile));

            multipartEntityBuilder.addPart(formBodyPartBuilder.build());

            HttpPost httpPost = new HttpPost(builder.build());
            httpPost.setEntity(multipartEntityBuilder.build());
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IllegalStateException("Geocode failed with error: " + responseBody);
                }
            }

            return parseResponse(responseBody);

        } catch (Exception e)
        {
            LogUtil.error(e.getMessage(), e);
            throw new IllegalStateException("Cannot fetch geocode info.", e);
        } finally
        {
            if (scratchFile != null && scratchFile.exists())
            {
                scratchFile.delete();
            }
        }
    }

    private Map<Long, LatLongCoordinates> parseResponse(String responseBody)
    {
        Map<Long, LatLongCoordinates> response = new HashMap<>();
        String[] lines = responseBody.split("\n");
        for (String line : lines)
        {
            try
            {
                String[] parts = line.split("\",\"");
                Long matrixUniqeId = Long.valueOf(parts[0].replace("\"", ""));
                if(parts.length > 5)
                {
                    String[] latLong = parts[5].replace("\"", "").split(",");

                    LatLongCoordinates coordinates = new LatLongCoordinates();
                    coordinates.setLatitude(Double.valueOf(latLong[1]));
                    coordinates.setLongitude(Double.valueOf(latLong[0]));

                    response.put(matrixUniqeId, coordinates);
                }
            }catch (Exception e)
            {
                LogUtil.error(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public GeocodeResponse geocodeOneLineAddress(String address)
    {
        LogUtil.debug("Looking up lat/Long for address: {}", address);
        try
        {
            URIBuilder builder = new URIBuilder("https://geocoding.geo.census.gov/geocoder/locations/onelineaddress");
            builder.addParameter("address", address);
            builder.addParameter("benchmark", "9");
            builder.addParameter("format", "json");

            HttpGet httpPost = new HttpGet(builder.build());
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());
                if (response.getStatusLine().getStatusCode() != 200)
                {
                    throw new IllegalStateException("Geocode failed with error: " + responseBody);
                }
            }

            return objectMapper.readValue(responseBody, GeocodeResponse.class);

        } catch (Exception e)
        {
            LogUtil.error(e.getMessage(), e);
            throw new IllegalStateException("Cannot fetch geocode Data for address: " + address, e);
        }
    }


}
