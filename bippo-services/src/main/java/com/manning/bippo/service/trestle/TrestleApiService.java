package com.manning.bippo.service.trestle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.pojo.TrestleApiData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * A service class for communicating with the Trestle API.
 *
 * TrestleApiData objects returned by request methods in this class are pages,
 * and as such they may have subsequent pages that can also be requested. Use
 * TrestleApiData.hasNextPage() to determine if there are subsequent pages, and
 * use TrestleApiService.requestNextPage(TrestleApiData) to request a next page.
 */
@Service
public class TrestleApiService {

    public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    private static final String OAUTH_URL = "https://api-prod.corelogic.com/trestle/oidc/connect/token";
    private static final String CLIENT_ID = "trestle_BIPPOLLCHomeInvestorTool20210119062938";
    private static final String CLIENT_SEC = "7f7df197fdee4304b1b105421415443e";
    private static final String PROPERTY_API_URL = "https://api-prod.corelogic.com/trestle/odata/Property";

    static {
        DATETIME.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private final CloseableHttpClient httpClient;
    private final Gson gson;
    private String token;
    private long tokenExpire;

    public TrestleApiService() {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        this.httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        this.gson = new GsonBuilder().create();
        this.token = null;
        this.tokenExpire = 0L;
    }

    public TrestleApiData requestNextPage(TrestleApiData data) {
        if (!data.hasNextPage()) {
            return null;
        }

        // Trestle likes to return spaces in their links, causing apache's HttpClient to reject these links if we don't manually sanitize them
        return this.tryExec(new HttpGet(data.getSanitizedNextLink()));
    }

    public TrestleApiData requestModifiedBetween(String originatingSystemName, Date begin, Date end) {
        final HttpGet get;

        try {
            get = new HttpGet(new URIBuilder(TrestleApiService.PROPERTY_API_URL)
                    .addParameter("$filter", String.format(
                            "(ModificationTimestamp ge %s and ModificationTimestamp lt %s and OriginatingSystemName eq '%s')",
                            TrestleApiService.DATETIME.format(begin),
                            TrestleApiService.DATETIME.format(end),
                            originatingSystemName))
                    .addParameter("$expand", "Media($select=MediaURL;$orderby=Order)").build());
        } catch (URISyntaxException ex) {
            LogUtil.error("Failed to generate Trestle API request", ex);
            throw new IllegalStateException(ex);
        }

        return this.tryExec(get);
    }

    public TrestleApiData requestListingId(String originatingSystemName, int listingId) {
        final HttpGet get;

        try {
            get = new HttpGet(new URIBuilder(TrestleApiService.PROPERTY_API_URL)
                    .addParameter("$filter", String.format(
                            "(ListingId eq '%d' and OriginatingSystemName eq '%s')",
                            listingId, originatingSystemName))
                    .addParameter("$expand", "Media($select=MediaURL;$orderby=Order)").build());
        } catch (URISyntaxException ex) {
            LogUtil.error("Failed to generate Trestle API request", ex);
            throw new IllegalStateException(ex);
        }

        return this.tryExec(get);
    }

    private TrestleApiData tryExec(HttpGet get) {
        try {
            get.addHeader("Authorization", "Bearer " + this.getToken());
            String text = null;

            try (final CloseableHttpResponse resp = this.httpClient.execute(get)) {
                text = EntityUtils.toString(resp.getEntity());

                if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                    LogUtil.error("Trestle API request failed with status: {} {}\n{}", resp.getStatusLine().getStatusCode(), resp.getStatusLine().getReasonPhrase(), text);
                    throw new IllegalStateException("Request Failed");
                }

                return this.gson.fromJson(text, TrestleApiData.class);
            } catch (JsonSyntaxException e) {
                LogUtil.error("Failed to deserialize Trestle response:\n{}", text);
                throw e;
            }
        } catch (IOException e) {
            LogUtil.error("Error during Trestle API request", e);
            throw new IllegalStateException(e);
        }
    }

    public synchronized String getToken() {
        if (System.currentTimeMillis() >= this.tokenExpire) {
            this.tryRegenerateToken();
        }

        return this.token;
    }

    private void tryRegenerateToken() {
        try {
            this.regenerateToken();
        } catch (IOException e) {
            LogUtil.error("Failed to generate OAuth token for Trestle API", e);
        }
    }

    private synchronized void regenerateToken() throws IOException {
        this.token = null;
        this.tokenExpire = 0L;

        final HttpPost post = new HttpPost(TrestleApiService.OAUTH_URL);
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("scope", "api"));
        params.add(new BasicNameValuePair("client_id", TrestleApiService.CLIENT_ID));
        params.add(new BasicNameValuePair("client_secret", TrestleApiService.CLIENT_SEC));
        post.setEntity(new UrlEncodedFormEntity(params));

        final JsonObject tokenResponse;

        try (final CloseableHttpResponse resp = this.httpClient.execute(post)) {
            if (resp.getStatusLine().getStatusCode() / 100 != 2) {
                LogUtil.error("Got " + resp.getStatusLine().getStatusCode() + " while requesting OAuth token from Trestle API");
                return;
            }

            tokenResponse = this.gson.fromJson(EntityUtils.toString(resp.getEntity()), JsonObject.class);
        }

        this.token = tokenResponse.get("access_token").getAsString();
        this.tokenExpire = System.currentTimeMillis() + tokenResponse.get("expires_in").getAsInt() * 1000 - 600000;

        if (!"Bearer".equals(tokenResponse.get("token_type").getAsString())) {
            LogUtil.warn("Unrecognized token_type {}, something may go wrong?", tokenResponse.get("token_type").getAsString());
        } else {
            LogUtil.info("Received Trestle API access token, using this token until " + TrestleApiService.DATETIME.format(this.tokenExpire));
        }
    }
}
