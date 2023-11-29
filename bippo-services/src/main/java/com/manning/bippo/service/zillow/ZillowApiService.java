package com.manning.bippo.service.zillow;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.manning.bippo.dao.ZillowPropertyRepository;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.ZillowProperty;
import com.manning.bippo.service.zillow.pojo.ZillowResults;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZillowApiService {

    private static final String RESULTS_TAG = "<results>";
    private static final String RESULTS_END = "</results>";
    
    private final CloseableHttpClient httpClient;
    private final XmlMapper xmlMapper;
    private String apiKey = "X1-ZWz1g5ishhscuj_8z2s9";

    @Autowired
    private ZillowPropertyRepository zillowRepository;

    public ZillowApiService() {
        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                    .setSocketTimeout(10_000)
                    .setConnectTimeout(10_000)
                    .build())
                .build();
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ZillowProperty findByAddressSemantics(AddressSemantics asProperty) {
        final ZillowProperty existing = this.zillowRepository.findFirstByFirstLineAndCityAndState(asProperty.getFirstLine(), asProperty.getCityName(), asProperty.getState());

        if (existing != null) {
            return existing;
        }

        final ZillowResults results = this.fetch(asProperty.getFirstLine(), asProperty.getCityName() + ", " + asProperty.getState());

        if (results != null && results.results.length > 0) {
            final ZillowProperty property = results.results[0].toNewZillowProperty();
            property.setSemantics(asProperty);
            this.zillowRepository.save(property);
            return property;
        }

        return null;
    }

    public ZillowAddressVerificiationResponse asAddressVerificiation(String address) {
        // This should work enough of the time as a last ditch effort to find information on an address
        // first line: [0, firstLine)
        // city: (firstLine, city)
        // state: (city, len), s/\d//g, trim
        final int firstLine = address.indexOf(','), city = address.indexOf(',', firstLine);

        if (firstLine < 0 || city < 0) {
            return null;
        }

        final ZillowProperty ret = this.findByAddress(address.substring(0, firstLine).trim(),
                address.substring(firstLine + 1, city).trim(), address.substring(city + 1).replaceAll("\\d", "").trim());
        return ret == null ? null : new ZillowAddressVerificiationResponse(ret);
    }

    /**
     * This method will look up and store Zillow data for the given address, but
     * it will not link it with an address_semantics row.
     * 
     * Prefer using ZillowApiService#findByAddressSemantics as it will link the
     * stored data to the given address_semantics property.
     */
    public ZillowProperty findByAddress(String firstLine, String city, String state) {
        final ZillowProperty existing = this.zillowRepository.findFirstByFirstLineAndCityAndState(firstLine, city, state);

        if (existing != null) {
            return existing;
        }

        final ZillowResults results = this.fetch(firstLine, city + ", " + state);

        if (results != null && results.results.length > 0) {
            final ZillowProperty property = results.results[0].toNewZillowProperty();
            this.zillowRepository.save(property);
            return property;
        }

        return null;
    }

    ZillowResults fetch(String address, String citystate) {
        try {
            URIBuilder builder = new URIBuilder("http://www.zillow.com/webservice/GetDeepSearchResults.htm");
            builder.addParameter("zws-id", this.apiKey);
            // Urlencode the address and city/state, except replace spaces with pluses first
            builder.addParameter("address", address);
            builder.addParameter("citystatezip", citystate);

            System.out.println("Zillow request url: " + builder.build());
            String responseBody;

            try (CloseableHttpResponse response = this.httpClient.execute(new HttpGet(builder.build()))) {
                responseBody = IOUtils.toString(response.getEntity().getContent());
    //            responseBody = responseBody.replaceAll(" currency=\"USD\"", "");
                System.out.println("Zillow response:\n" + responseBody);

                if (response.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IllegalStateException("Zillow API: Error " + response.getStatusLine().getStatusCode());
                }
            }

            final int beg = responseBody.indexOf(RESULTS_TAG), end = responseBody.indexOf(RESULTS_END);

            if ((beg | end) < 0) {
                throw new IllegalStateException("Zillow API: Got malformed/incomplete response");
            }

            // Trim the xml data to eliminate a lot of clutter.. we only need what's inside the <results> tag
            return this.xmlMapper.readValue(responseBody.substring(beg, end + RESULTS_END.length()), ZillowResults.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
