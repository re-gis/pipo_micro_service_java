package com.manning.bippo.service.oi.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.OnBoardInformaticsAnalyticsResponseRepository;
import com.manning.bippo.dao.OnBoardInformaticsAreaResponseRepository;
import com.manning.bippo.dao.OnBoardInformaticsMortgageOwnerResponseRepository;
import com.manning.bippo.dao.OnBoardInformaticsPoiResponseRepository;
import com.manning.bippo.dao.OnBoardInformaticsResponseRepository;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.OnBoardInformaticsAnalyticsResponse;
import com.manning.bippo.dao.pojo.OnBoardInformaticsAreaResponse;
import com.manning.bippo.dao.pojo.OnBoardInformaticsMortgageOwnerResponse;
import com.manning.bippo.dao.pojo.OnBoardInformaticsPoiResponse;
import com.manning.bippo.dao.pojo.OnBoardInformaticsResponse;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.oi.pojo.AddressPoiPropertyResponse;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import com.manning.bippo.service.oi.pojo.AnalyticsMultiResponse;
import com.manning.bippo.service.oi.pojo.AnalyticsResponse;
import com.manning.bippo.service.oi.pojo.AreaFullPropertyResponse;
import com.manning.bippo.service.oi.pojo.DetailWithSchoolsResponse;
import com.manning.bippo.service.oi.pojo.MortgageOwnerPropertyResponse;
import com.manning.bippo.service.oi.pojo.OIPropertySearchFilter;
import com.manning.bippo.service.oi.pojo.SnapshotResponse;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OnBoardInformaticsPropertyServiceImpl implements OnBoardInformaticsPropertyService
{
    /*
     * TODO: All of the get_Raw(..) methods in this class need to be
     * re-organized so their common functionality can be shared.
     *
     * Additionally, those same methods should probably be changed to
     * throw exceptions as opposed to returning null.
     */
    private static final int TIMEOUT_IN_SECONDS = 60;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Autowired
    OnBoardInformaticsResponseRepository onBoardInformaticsResponseRepository;

    @Autowired
    OnBoardInformaticsAreaResponseRepository onBoardInformaticsAreaResponseRepository;
    
    @Autowired
    OnBoardInformaticsPoiResponseRepository onBoardInformaticsPoiResponseRepository;

    @Autowired
    OnBoardInformaticsAnalyticsResponseRepository onBoardInformaticsAnalyticsResponseRepository;

    @Autowired
    OnBoardInformaticsMortgageOwnerResponseRepository onBoardInformaticsMortgageOwnerResponseRepository;

    private String apiKey = "b2846f5c76751b4f2eb736b09b89554d";

    public OnBoardInformaticsPropertyServiceImpl()
    {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_IN_SECONDS * 1000)
                .setConnectTimeout(TIMEOUT_IN_SECONDS * 1000)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Override
    public SnapshotResponse getAllEvents(OIPropertySearchFilter filter, int page, int size)
    {
        try {
            URIBuilder builder = new URIBuilder("https://search.onboard-apis.com/propertyapi/v1.0.0/property/snapshot");
            builder.addParameter("page", Integer.toString(page));
            builder.addParameter("pageSize", Integer.toString(size));

            addQueryParams(filter, builder);

            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader(new BasicHeader("apiKey", apiKey));
            httpGet.setHeader(new BasicHeader("accept", "application/json"));
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());
                LogUtil.debug(responseBody);

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IllegalStateException("Onboard API failed with error: " + responseBody);
                }
            }

            return objectMapper.readValue(responseBody, SnapshotResponse.class);
        } catch (URISyntaxException | IOException e) {
            LogUtil.error(e.getMessage(), e);
        }

        return null;
    }

    private void addQueryParams(OIPropertySearchFilter filter, URIBuilder builder)
    {
        Field[] declaredFields = filter.getClass().getDeclaredFields();

        for (Field declaredFiled : declaredFields) {
            try {
                Object value = declaredFiled.get(filter);


                if (value != null && !String.valueOf(value).isEmpty()) {
                    builder.addParameter(declaredFiled.getName(), String.valueOf(value));
                }

            } catch (IllegalAccessException e) {
                LogUtil.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public String getAllEventsRaw(String apn, String fips, String primaryNumber, String streetName, String cityName)
    {
        try {
            URIBuilder builder = new URIBuilder("https://search.onboard-apis.com/propertyapi/v1.0.0/allevents/detail");
            builder.addParameter("APN", apn);
            builder.addParameter("FIPS", fips);

            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader(new BasicHeader("apiKey", apiKey));
            httpGet.setHeader(new BasicHeader("accept", "application/json"));
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IllegalStateException("Onboard API failed with error: " + responseBody);
                }
            }

//            LogUtil.debug(responseBody);
            AllEventsPropertyResponse parsed = this.convertJSONToAllEventsResponse(responseBody);

            if (parsed != null && parsed.property != null) {
                /*
                 * Searching by APN can yield multiple results as APNs are not univerally unique.
                 * The only proper solution to this is to use the APN in conjunction with the FIPS code,
                 * but we do not have access to the FIPS until after we have requested this data from onboard.
                 */
                List<AllEventsPropertyResponse.Property> results = Lists.newArrayList(parsed.property);

                for (final Iterator<AllEventsPropertyResponse.Property> it = results.iterator(); it.hasNext(); ) {
                    AllEventsPropertyResponse.Property res = it.next();

                    if (res != null && res.address != null && res.address.line1 != null && res.address.line2 != null) {
                        String line1 = res.address.line1.toLowerCase();

                        // Searching by address is subject to many near-miss errors due to spelling, etc
                        // Using the number, street, and city are a lenient way to narrow down to the result we wanted (assuming it is present)
                        if (line1.contains(primaryNumber.toLowerCase()) && line1.contains(streetName.toLowerCase())
                                && res.address.line2.toLowerCase().contains(cityName.toLowerCase())) {
                            continue;
                        }
                    }

                    it.remove();
                }

                if (results.isEmpty()) {
                    throw new NoSuchElementException("All AllEventsPropertyResponse.Property results in this APN search were rejected!");
                }

                parsed.property = results.toArray(new AllEventsPropertyResponse.Property[results.size()]);
                responseBody = this.objectMapper.writeValueAsString(parsed);
            }

            return responseBody;
        } catch (URISyntaxException | IOException e) {
            LogUtil.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getAllEventsRaw(String firstLine, String secondLine)
    {
        try {
            URIBuilder builder = new URIBuilder("https://search.onboard-apis.com/propertyapi/v1.0.0/allevents/detail");
            builder.addParameter("address1", firstLine);
            builder.addParameter("address2", secondLine);

            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader(new BasicHeader("apiKey", apiKey));
            httpGet.setHeader(new BasicHeader("accept", "application/json"));
            String responseBody;

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                responseBody = IOUtils.toString(response.getEntity().getContent());
                LogUtil.debug(responseBody);

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IllegalStateException("Onboard API failed with error: " + responseBody);
                }
            }

            return responseBody;
        } catch (URISyntaxException | IOException e) {
            LogUtil.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public AllEventsPropertyResponse getAllEvents(String firstLine, String secondLine)
    {
        try {
            return objectMapper.readValue(getAllEventsRaw(firstLine, secondLine), AllEventsPropertyResponse.class);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public AreaFullPropertyResponse getAreaFull(String areacode)
    {
        try {
            return objectMapper.readValue(getAreaFullRaw(areacode), AreaFullPropertyResponse.class);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public AreaFullPropertyResponse getAreaFull(int zipcode)
    {
        return this.getAreaFull("ZI" + zipcode);
    }

    @Override
    public String getAreaFullRaw(String areacode)
    {
        try {
            final URIBuilder builder = new URIBuilder("https://search.onboard-apis.com/communityapi/v2.0.0/area/full");
            builder.addParameter("AreaId", areacode);

            final HttpGet get = new HttpGet(builder.build());
            get.setHeader(new BasicHeader("apiKey", apiKey));
            get.setHeader(new BasicHeader("accept", "application/json"));

            try (CloseableHttpResponse resp = httpClient.execute(get)) {
                final String body = IOUtils.toString(resp.getEntity().getContent());

                if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IllegalStateException("Onboard area/full API failed with error: " + body);
                }

                return body;
            }
        } catch (Exception e) {}

        return null;
    }

    @Override
    public AnalyticsResponse getAnalytics(String areacode, int year)
    {
        try {
            return objectMapper.readValue(getAnalyticsRaw(areacode, year), AnalyticsResponse.class);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public AnalyticsResponse getAnalytics(int zipcode, int year)
    {
        return this.getAnalytics("ZI" + zipcode, year);
    }

    @Override
    public String getAnalyticsRaw(String areacode, int year)
    {
        try {
            final URIBuilder builder = new URIBuilder("https://api.gateway.attomdata.com/propertyapi/v1.0.0/salestrend/snapshot");
            builder.addParameter("geoid", areacode);
            builder.addParameter("interval", "quarterly");
            builder.addParameter("startmonth", "january");
            builder.addParameter("endmonth", "december");
            builder.addParameter("startyear", Integer.toString(year));
            builder.addParameter("endyear", Integer.toString(year));

            final HttpGet get = new HttpGet(builder.build());
            get.setHeader(new BasicHeader("apiKey", apiKey));
            get.setHeader(new BasicHeader("accept", "application/json"));

            try (CloseableHttpResponse resp = httpClient.execute(get)) {
                final String body = IOUtils.toString(resp.getEntity().getContent());

                if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IllegalStateException("Onboard salestrend/snapshot API failed with error: " + body);
                }

                return body;
            }
        } catch (Exception e) {
            LogUtil.error("Onboard salestrend/snapshot API failed with error:", e);
        }

        return null;
    }

    @Override
    public String getAddressPoiRaw(String address)
    {
        try {
            final URIBuilder builder = new URIBuilder("https://search.onboard-apis.com/poisearch/v2.0.0/poi/street+address");
            builder.addParameter("StreetAddress", address);
            builder.addParameter("SearchDistance", "10");
            builder.addParameter("RecordLimit", "100");
            builder.addParameter("Sort", "NAME");

            final HttpGet get = new HttpGet(builder.build());
            get.setHeader(new BasicHeader("apiKey", apiKey));
            get.setHeader(new BasicHeader("accept", "application/json"));

            try (CloseableHttpResponse resp = httpClient.execute(get)) {
                final String body = IOUtils.toString(resp.getEntity().getContent());

                if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IllegalStateException("Onboard poi/street+address API failed with error: " + body);
                }

                return body;
            }
        } catch (Exception e) {
            LogUtil.error("Onboard poi/street+address API failed with error:", e);
        }

        return null;
    }

    @Override
    public String getMortgageOwnerRaw(long attomId)
    {
        try {
            final URIBuilder builder = new URIBuilder("https://api.gateway.attomdata.com/propertyapi/v1.0.0/property/detailmortgageowner");
            builder.addParameter("attomid", Long.toString(attomId));

            final HttpGet get = new HttpGet(builder.build());
            get.setHeader(new BasicHeader("apiKey", apiKey));
            get.setHeader(new BasicHeader("accept", "application/json"));

            try (CloseableHttpResponse resp = httpClient.execute(get)) {
                final String body = IOUtils.toString(resp.getEntity().getContent());

                if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IllegalStateException("Onboard property/detailmortgageowner API failed with error: " + body);
                }

                return body;
            }
        } catch (Exception e) {
            LogUtil.error("Onboard property/detailmortgageowner API failed with error:", e);
        }

        return null;
    }

    @Override
    public OnBoardInformaticsResponse findById(String obPropId)
    {
        List<OnBoardInformaticsResponse> firstByObPropId = onBoardInformaticsResponseRepository.findByObPropId("%obPropId\":" + obPropId + "%");

        if (firstByObPropId != null && firstByObPropId.size() > 1) {
            return firstByObPropId.get(firstByObPropId.size() - 1);
        } else {
            try {
                return downloadAllEventsResponse(obPropId);
            } catch (URISyntaxException | IOException e) {
                LogUtil.error(e.getMessage(), e);
            }
        }

        return null;
    }

    private OnBoardInformaticsResponse downloadAllEventsResponse(String obPropId) throws URISyntaxException, IOException
    {
        URIBuilder builder = new URIBuilder("https://search.onboard-apis.com/propertyapi/v1.0.0/allevents/detail");
        builder.addParameter("id", obPropId);


        HttpGet httpGet = new HttpGet(builder.build());
        httpGet.setHeader(new BasicHeader("apiKey", apiKey));
        httpGet.setHeader(new BasicHeader("accept", "application/json"));
        String responseBody;

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            responseBody = IOUtils.toString(response.getEntity().getContent());
            LogUtil.debug(responseBody);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IllegalStateException("Onboard API failed with error: " + responseBody);
            }
        }

//        AllEventsPropertyResponse allEventsPropertyResponse = objectMapper.readValue(responseBody, AllEventsPropertyResponse.class);
        OnBoardInformaticsResponse onBoardInformaticsResponse = new OnBoardInformaticsResponse();
        onBoardInformaticsResponse.setResponse(responseBody);
        return onBoardInformaticsResponseRepository.save(onBoardInformaticsResponse);
    }

    @Override
    public AllEventsPropertyResponse findByAddressSemantics(AddressSemantics addressSemantics, boolean allowOldData) {
        AllEventsPropertyResponse resp = convertJSONToAllEventsResponse(downloadAllEventsPropertyData(addressSemantics));

        if (!allowOldData && resp != null && resp.property != null && resp.property.length > 0 &&
                resp.property[0] != null && resp.property[0].identifier != null && resp.property[0].identifier.attomId < 1L) {
            resp = convertJSONToAllEventsResponse(this.redownloadAllEventsPropertyData(addressSemantics));
        }

        return resp;
    }

    @Override
    public AllEventsPropertyResponse findByNtreisProperty(NtreisProperty ntreisProperty, boolean queueIfNotPresent)
    {
        String allEvents = null;

        if (queueIfNotPresent) {
            // Why does queueIfNotPresent not check if the data is already present before queuing?
            rabbitMQSenderService.queueDownloadOnBoardData(ntreisProperty.getId());
        } else {
            allEvents = downloadAllEventsPropertyData(ntreisProperty);
        }

        AllEventsPropertyResponse resp = convertJSONToAllEventsResponse(allEvents);

        if (!queueIfNotPresent && resp != null && resp.property != null && resp.property.length > 0 &&
                resp.property[0] != null && resp.property[0].identifier != null && resp.property[0].identifier.attomId < 1L) {
            this.redownloadAllEventsPropertyData(ntreisProperty);
        }

        return resp;
    }

    @Override
    public AllEventsPropertyResponse findByAbstractProperty(AbstractProperty abstractProperty, boolean queueIfNotPresent)
    {
        if (abstractProperty instanceof NtreisProperty) {
            return this.findByNtreisProperty((NtreisProperty) abstractProperty, queueIfNotPresent);
        } else if (abstractProperty instanceof TrestleProperty) {
            return null;
//            return this.findByTrestleProperty((TrestleProperty) abstractProperty);
        }

        throw new UnsupportedOperationException("Unrecognized AbstractProperty implementation for tax data.");
    }

    @Override
    public OnBoardInformaticsResponse downloadAllEventsPropertyData(AddressSemantics property)
    {
        final String firstLine = property.getFirstLine(), secondLine = property.getLastLine();

        {
            final OnBoardInformaticsResponse previous = property.getTaxId();

            // TODO: If previous download was from a previous year, we need to re-download.
            if (previous != null && previous.getResponse() != null) {
                LogUtil.info("Already downloaded the AllEvents info for {}, {}...", firstLine, secondLine);
                return previous;
            }
        }

        LogUtil.info("Downloading AllEvents for property {}, {} ...", firstLine, secondLine);

        OnBoardInformaticsResponse onBoardInformaticsResponse = new OnBoardInformaticsResponse();
        onBoardInformaticsResponse.setNtreisProperty(null);

        boolean usageLimitError = false;
        String allEvents = null;

        try {
            allEvents = getAllEventsRaw(firstLine, secondLine);
            onBoardInformaticsResponse.setResponse(allEvents);
        } catch (Exception e) {
            if (e.getMessage().contains("Usage limits are exceeded with")) {
                usageLimitError = true;
            }

            LogUtil.error(e.getMessage(), e);
        } finally {
            // Save only if we have not encountered an error with usage limits
            if (!usageLimitError) {
                onBoardInformaticsResponseRepository.save(onBoardInformaticsResponse);
            }
        }

        return usageLimitError ? null : onBoardInformaticsResponse;
    }

    @Override
    public OnBoardInformaticsResponse redownloadAllEventsPropertyData(AddressSemantics property)
    {
        final String firstLine = property.getFirstLine(), secondLine = property.getLastLine();
        OnBoardInformaticsResponse obir = property.getTaxId();

        LogUtil.info("Redownloading AllEvents for property {}, {} ...", firstLine, secondLine);

        if (obir == null) {
            obir = new OnBoardInformaticsResponse();
        }

        boolean usageLimitError = false;
        String allEvents = null;

        try {
            allEvents = getAllEventsRaw(firstLine, secondLine);
            obir.setResponse(allEvents);
        } catch (Exception e) {
            if (e.getMessage().contains("Usage limits are exceeded with")) {
                usageLimitError = true;
            }

            LogUtil.error(e.getMessage(), e);
        } finally {
            // Save only if we have not encountered an error with usage limits
            if (!usageLimitError) {
                obir = onBoardInformaticsResponseRepository.save(obir);
            }
        }

        return usageLimitError ? null : obir;
    }

    @Override
    public String redownloadAllEventsPropertyData(NtreisProperty ntreisProperty)
    {
        final String firstLine = ntreisProperty.getAddressLine1(), secondLine = ntreisProperty.getAddressLine2();
        OnBoardInformaticsResponse obir = onBoardInformaticsResponseRepository.findFirstByNtreisPropertyOrderByCreatedDesc(ntreisProperty);

        LogUtil.info("Downloading AllEvents for property {}, {} ...", firstLine, secondLine);

        if (obir == null) {
            obir = new OnBoardInformaticsResponse();
            obir.setNtreisProperty(ntreisProperty);
        }

        boolean usageLimitError = false;
        String allEvents = null;

        try {
            allEvents = getAllEventsRaw(firstLine, secondLine);
            obir.setResponse(allEvents);
        } catch (Exception e) {
            if (e.getMessage().contains("Usage limits are exceeded with")) {
                usageLimitError = true;
            }

            LogUtil.error(e.getMessage(), e);
        } finally {
            // Save only if the error is not about usage limits
            if (!usageLimitError) {
                onBoardInformaticsResponseRepository.save(obir);
            }
        }

        return allEvents;
    }

    @Override
    public String downloadAllEventsPropertyData(NtreisProperty ntreisProperty)
    {
        final String firstLine = ntreisProperty.getAddressLine1(), secondLine = ntreisProperty.getAddressLine2();

        {
            final OnBoardInformaticsResponse previous = onBoardInformaticsResponseRepository.findFirstByNtreisPropertyOrderByCreatedDesc(ntreisProperty);

            // TODO: If previous download was from a previous year, we need to re-download.
            if (previous != null && previous.getResponse() != null) {
                LogUtil.info("Already downloaded the AllEvents info for {}, {}...", firstLine, secondLine);
                return previous.getResponse();
            }
        }

        LogUtil.info("Downloading AllEvents for property {}, {} ...", firstLine, secondLine);

        OnBoardInformaticsResponse onBoardInformaticsResponse = new OnBoardInformaticsResponse();
        onBoardInformaticsResponse.setNtreisProperty(ntreisProperty);

        boolean usageLimitError = false;
        String allEvents = null;

        try {
            allEvents = getAllEventsRaw(firstLine, secondLine);
            onBoardInformaticsResponse.setResponse(allEvents);
        } catch (Exception e) {
            if (e.getMessage().contains("Usage limits are exceeded with")) {
                usageLimitError = true;
            }

            LogUtil.error(e.getMessage(), e);
        } finally {
            // Save only if the error is not about usage limits
            if (!usageLimitError) {
                onBoardInformaticsResponseRepository.save(onBoardInformaticsResponse);
            }
        }

        return allEvents;
    }

    @Override
    public AreaFullPropertyResponse downloadAreaFullPropertyResponse(int zipcode)
    {
        final String raw = this.downloadAreaFullPropertyData(zipcode);

        try {
            return raw == null ? new AreaFullPropertyResponse() : objectMapper.readValue(raw, AreaFullPropertyResponse.class);
        } catch (Exception e) {
            LogUtil.error("Failed to deserialize AreaFullPropertyResponse", e);
            return new AreaFullPropertyResponse();
        }
    }

    @Override
    public AddressPoiPropertyResponse downloadAddressPoiPropertyResponse(String address)
    {
        final String raw = this.downloadAddressPoiPropertyData(address);

        try {
            return raw == null ? new AddressPoiPropertyResponse() : objectMapper.readValue(raw, AddressPoiPropertyResponse.class);
        } catch (Exception e) {
            LogUtil.error("Failed to deserialize AreaFullPropertyResponse", e);
            return new AddressPoiPropertyResponse();
        }
    }

    @Override
    public MortgageOwnerPropertyResponse downloadMortgageOwnerPropertyResponse(long attomId)
    {
        final String raw = this.downloadMortgageOwnerPropertyData(attomId);

        try {
            return raw == null ? new MortgageOwnerPropertyResponse() : objectMapper.readValue(raw, MortgageOwnerPropertyResponse.class);
        } catch (Exception e) {
            LogUtil.error("Failed to deserialize MortgageOwnerPropertyResponse", e);
            return new MortgageOwnerPropertyResponse();
        }
    }

    @Override
    public String downloadAreaFullPropertyData(int zipcode)
    {
        OnBoardInformaticsAreaResponse existing = onBoardInformaticsAreaResponseRepository.findFirstByAreaCode("ZI" + zipcode);

        if (existing != null && existing.getResponse() != null) {
            LogUtil.info("Already downloaded AreaFull for zipcode {}.", zipcode);
            return existing.getResponse();
        }

        LogUtil.info("Downloading AreaFull for zipcode {}..", zipcode);

        OnBoardInformaticsAreaResponse areaResponse = existing == null ? new OnBoardInformaticsAreaResponse() : existing;
        areaResponse.setAreaCode("ZI" + zipcode);

        final String areaFull;
        boolean usageLimitError = false;

        try {
            areaFull = getAreaFullRaw("ZI" + zipcode);
            areaResponse.setResponse(areaFull);
        } catch (Exception e) {
            usageLimitError = e.getMessage().contains("Usage limits are exceeded with");
            LogUtil.error(e.getMessage(), e);
            return null;
        } finally {
            if (!usageLimitError) {
                onBoardInformaticsAreaResponseRepository.save(areaResponse);
            }
        }
        return areaFull;
    }

    @Override
    public String downloadAddressPoiPropertyData(String address)
    {
        OnBoardInformaticsPoiResponse existing = onBoardInformaticsPoiResponseRepository.findFirstByAddress(address);

        if (existing != null && existing.getResponse() != null) {
            LogUtil.info("Already downloaded AddressPoi for address {}.", address);
            return existing.getResponse();
        }

        LogUtil.info("Downloading AddressPoi for address {}..", address);

        OnBoardInformaticsPoiResponse areaResponse = existing == null ? new OnBoardInformaticsPoiResponse() : existing;
        areaResponse.setAddress(address);

        final String addressPoi;
        boolean usageLimitError = false;

        try {
            addressPoi = getAddressPoiRaw(address);
            areaResponse.setResponse(addressPoi);
        } catch (Exception e) {
            usageLimitError = e.getMessage().contains("Usage limits are exceeded with");
            LogUtil.error(e.getMessage(), e);
            return null;
        } finally {
            if (!usageLimitError) {
                onBoardInformaticsPoiResponseRepository.save(areaResponse);
            }
        }

        return addressPoi;
    }

    @Override
    public String downloadMortgageOwnerPropertyData(long attomId)
    {
        OnBoardInformaticsMortgageOwnerResponse existing = onBoardInformaticsMortgageOwnerResponseRepository.findFirstByAttomId(attomId);

        if (existing != null && existing.getResponse() != null) {
            LogUtil.info("Already downloaded MortgageOwner for ATTOM ID {}.", attomId);
            return existing.getResponse();
        }

        LogUtil.info("Downloading MortgageOwner for ATTOM ID {}..", attomId);

        OnBoardInformaticsMortgageOwnerResponse moResponse = existing == null ? new OnBoardInformaticsMortgageOwnerResponse() : existing;
        moResponse.setAttomId(attomId);

        final String mo;
        boolean usageLimitError = false;

        try {
            mo = getMortgageOwnerRaw(attomId);
            moResponse.setResponse(mo);
        } catch (Exception e) {
            usageLimitError = e.getMessage().contains("Usage limits are exceeded with");
            LogUtil.error(e.getMessage(), e);
            return null;
        } finally {
            if (!usageLimitError) {
                onBoardInformaticsMortgageOwnerResponseRepository.save(moResponse);
            }
        }

        return mo;
    }

/*  public AnalyticsMultiResponse getAnalyticsFullResponse(int zipcode)
    {
        // TODO: Onboard has not yet released this API; in the future, this should request onboard when the data is not cached in the database.
        final int yearMax = LocalDateTime.now().getYear();
        final List<OnBoardInformaticsAnalyticsResponse> found = this.onBoardInformaticsAnalyticsResponseRepository
                .findByZipAndYearBetweenOrderByYearAscQuarterAsc(zipcode, yearMax - 3, yearMax - 1); // This is a SQL BETWEEN, so inclusive
        final AnalyticsMultiResponse resp = new AnalyticsMultiResponse();

        if (found == null || found.isEmpty()) {
            return resp;
        }

        for (OnBoardInformaticsAnalyticsResponse quarter : found) {
            final int quarterNumber;

            switch (quarterNumber = quarter.getQuarter()) {
                case 1: case 2: case 3: case 4: // Use quarters 1 through 4 as is
                    resp.withQuarter(quarter.getYear(), quarterNumber, quarter.getPrice(), quarter.getCount());
                    break;
                case 9: // An entry for "quarter 9" is present to represent a year-total summary
                    resp.withYear(quarter.getYear(), quarter.getPrice(), quarter.getCount());
                    break;
            }
        }

        return resp;
    }*/

    List<OnBoardInformaticsAnalyticsResponse> saveAnalytics(int zip, AnalyticsResponse ar) {
        if (ar == null || ar.salestrends == null) {
            return Collections.emptyList();
        }

        final List<OnBoardInformaticsAnalyticsResponse> saved = new ArrayList<>();

        for (AnalyticsResponse.SalesTrends trend : ar.salestrends) {
            if (trend.daterange != null && trend.salestrend != null) {
                final int year = trend.daterange.getStartYear(), quarter = trend.daterange.getStartQuarter();
                OnBoardInformaticsAnalyticsResponse analytics = this.onBoardInformaticsAnalyticsResponseRepository.findTopByZipAndYearAndQuarter(zip, year, quarter);

                if (analytics == null) {
                    analytics = new OnBoardInformaticsAnalyticsResponse();
                    analytics.setCreated(new Date());
                }

                analytics.setZip(zip);
                analytics.setYear(year);
                analytics.setQuarter(quarter);
                analytics.setCount(trend.salestrend.homesalecount);
                analytics.setPrice(trend.salestrend.avgsaleprice);
                saved.add(this.onBoardInformaticsAnalyticsResponseRepository.save(analytics));
            }
        }

        LogUtil.info("Onboard: Saved {} analytics entries from {}", saved.size(), ar.salestrends.length);
        return saved;
    }

    @Override
    public AnalyticsMultiResponse downloadAnalyticsResponse(int zipcode)
    {
        final int currentYear = LocalDateTime.now().getYear();
        final AnalyticsMultiResponse resp = new AnalyticsMultiResponse();

        for (int year = currentYear - 3; year < currentYear; year++) {
            List<OnBoardInformaticsAnalyticsResponse> found = this.onBoardInformaticsAnalyticsResponseRepository.findByZipAndYearOrderByQuarterAsc(zipcode, year);
            LogUtil.info("Analytics: Got {} for {} {}", found != null ? found.size() : 0, zipcode, year);

            if (found == null || found.isEmpty()) {
                final AnalyticsResponse yearResp = this.getAnalytics(zipcode, year);
                found = this.saveAnalytics(zipcode, yearResp);
                LogUtil.info("Analytics: After request we have {} for {} {}", found != null ? found.size() : 0, zipcode, year);
            }

            if (found != null && !found.isEmpty()) {
                for (OnBoardInformaticsAnalyticsResponse quarter : found) {
                    final int quarterNumber;

                    switch (quarterNumber = quarter.getQuarter()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            resp.withQuarter(quarter.getYear(), quarterNumber, quarter.getPrice(), quarter.getCount());
                            break;
                        case 9:
                            resp.withYear(quarter.getYear(), quarter.getPrice(), quarter.getCount());
                            break;
                    }
                }
            }
        }

        return resp;
    }

    @Override
    public String getDetailWithSchoolsRaw(long attomId)
    {
        try {
            final URIBuilder builder = new URIBuilder("https://api.gateway.attomdata.com/propertyapi/v1.0.0/property/detailwithschools");
            builder.addParameter("attomid", Long.toString(attomId));

            final HttpGet get = new HttpGet(builder.build());
            get.setHeader(new BasicHeader("apiKey", apiKey));
            get.setHeader(new BasicHeader("accept", "application/json"));

            try (CloseableHttpResponse resp = httpClient.execute(get)) {
                final String body = IOUtils.toString(resp.getEntity().getContent());

                if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IllegalStateException("Onboard property/detailwithschools API failed with error: " + body);
                }

                return body;
            }
        } catch (Exception e) {
            LogUtil.error("Onboard property/detailwithschools API failed with error:", e);
        }

        return null;
    }

    @Override
    public DetailWithSchoolsResponse downloadDetailWithSchoolsResponse(AllEventsPropertyResponse resp)
    {
        final AllEventsPropertyResponse.Property prop;
        final long attomId;

        if (resp == null || resp.property == null || resp.property.length < 1
                || (prop = resp.property[0]) == null || prop.identifier == null
                || (attomId = prop.identifier.attomId) < 1L) {
            return null;
        }

        return this.downloadDetailWithSchoolsResponse(attomId);
    }

    @Override
    public DetailWithSchoolsResponse downloadDetailWithSchoolsResponse(long attomId) {
        try {
            return objectMapper.readValue(getDetailWithSchoolsRaw(attomId), DetailWithSchoolsResponse.class);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public OnBoardInformaticsResponse getOnBoardInformaticsResponse(String firstLine, String secondLine)
    {
        OnBoardInformaticsResponse response = new OnBoardInformaticsResponse();
        response.setResponse(getAllEventsRaw(firstLine, secondLine));

        return onBoardInformaticsResponseRepository.save(response);
    }

    @Override
    public OnBoardInformaticsResponse getOnBoardInformaticsResponse(String apn, String fips, String primaryNumber, String streetName, String cityName)
    {
        OnBoardInformaticsResponse response = new OnBoardInformaticsResponse();
        response.setResponse(getAllEventsRaw(apn, fips, primaryNumber, streetName, cityName));

        return onBoardInformaticsResponseRepository.save(response);
    }

    public AllEventsPropertyResponse convertJSONToAllEventsResponse(OnBoardInformaticsResponse obir)
    {
        return obir == null ? null : this.convertJSONToAllEventsResponse(obir.getResponse());
    }

    @Override
    public AllEventsPropertyResponse convertJSONToAllEventsResponse(String response)
    {
        try {
            if (response != null) {
                return objectMapper.readValue(response, AllEventsPropertyResponse.class);
            }
        } catch (Exception e) {
            LogUtil.debug(e.getMessage(), e);
        }

        return null;
    }
}
