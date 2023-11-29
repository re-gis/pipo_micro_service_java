package com.manning.bippo.service.rets.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.rets.pojo.RetsResponse;
import org.realtors.rets.client.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AbstractRetsService
{
    // New URL, required as of Jan 2019      https://matrixrets.ntreis.net/rets/login.ashx
    private static final String LOGIN_URL = "https://matrixrets.ntreis.net/rets/login.ashx";
    private static final String USERNAME = "0435320_NID";
    private static final String PASSWORD = "WYr9qk&3?";

    RetsHttpClient httpClient = new CommonsHttpClient();
    RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
    RetsSession session = null;

    protected synchronized int getQueryCount(String sQuery) throws RetsException
    {
        String sResource = "Property";
        String sClass = "Listing";

        //Create a SearchRequest
        SearchRequest request = new SearchRequest(sResource, sClass, sQuery);
        request.setFormatCompactDecoded();

        //Set request to retrive count if desired
        request.setCountFirst();
//        LogUtil.debug("Count query running for: {}" + sQuery);

        return getSession().getQueryCount(request);
    }

    protected Page<RetsResponse> queryRetsFeed(String sQuery, int page, int size)
    {
        return queryRetsFeed(sQuery, RetsResponse.searchFields(), page, size);
    }

    protected synchronized Page<RetsResponse> queryRetsFeed(String sQuery, String searchFields, int page, int size)
    {
        String sResource = "Property";
        String sClass = "Listing";

        // Create a SearchRequest
        SearchRequest request = new SearchRequest(sResource, sClass, sQuery);
        request.setFormatCompactDecoded();

        if (size > 0) {
            request.setLimit(size);
            request.setOffset(page * size);
        }

        // Select only available fields
        request.setSelect(searchFields);

        // Set request to retrive count if desired
        request.setCountFirst();

        SearchResultImpl response;
        List<RetsResponse> retsResponseList = new ArrayList<>();

        try {
            // Execute the search
            response = (SearchResultImpl) getSession().search(request);

            // Print out count and columns
            int count = response.getCount();

            // Iterate over, print records
            for (int row = 0; row < response.getRowCount(); row++) {
                RetsResponse retsResponse = new RetsResponse();

                for (int columnCount = 0; columnCount < response.getRow(row).length; columnCount++) {
                    try {
                        Class<? extends RetsResponse> ntreisResponseClass = retsResponse.getClass();
                        Field ntreisResponseClassField = ntreisResponseClass.getDeclaredField(response.getColumns()[columnCount]);
                        ntreisResponseClassField.set(retsResponse, response.getRow(row)[columnCount]);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        LogUtil.error(e.getMessage(), e);
                    }
                }

                retsResponseList.add(retsResponse);
            }

            if (size > 0) {
                return new PageImpl<>(retsResponseList, new PageRequest(page, size), count);
            } else {
                return new PageImpl<>(retsResponseList, new PageRequest(1, count > 0 ? count : 10), count);
            }
        } catch (RetsException e) {
            if (e.getMessage().contains("Unauthorized Access Denied")) {
                initializeSession();
            }

            final StringBuilder emsg = new StringBuilder("Error querying RETS feed: ");
            emsg.append(e.getMessage())
                    .append("\nResource: Property | Class : Listing | Query: ").append(sQuery)
                    .append("\nLimit: ").append(size).append(" | Offset: ").append(page * size)
                    .append("\nSelect: ").append(searchFields);
            LogUtil.error(emsg.toString(), e);
        } catch (Exception e) {
            LogUtil.error("Critical error querying RETS feed for " + request.getUrl(), e);
        }

        LogUtil.info("Failure but found {} MLS records.", retsResponseList.size());
        return new PageImpl<>(Collections.emptyList(), new PageRequest(1, 10), 0);
    }

    public synchronized GetObjectResponse getFrontPhoto(Set<String> retsIds, String objectType) throws RetsException {
        GetObjectRequest getObjectRequest = new GetObjectRequest("Property", objectType);
        retsIds.forEach(retsId -> getObjectRequest.addObject(retsId, "0"));

        return getSession().getObject(getObjectRequest);
    }


    public synchronized GetObjectResponse getAllPhotos(Set<String> retsIds, String objectType) throws RetsException {
        GetObjectRequest getObjectRequest = new GetObjectRequest("Property", objectType);
        retsIds.forEach(retsId -> getObjectRequest.addObject(retsId, "*"));

        return getSession().getObject(getObjectRequest);
    }

    protected void initializeSession()
    {
        //Create a RetesSession with RetsHttpClient
        session = new RetsSession(LOGIN_URL, httpClient, retsVersion);

        //Set method as GET or POST
        session.setMethod("POST");
        LoginResponse loginResponse;
        try
        {
            loginResponse = session.login(USERNAME, PASSWORD);

        } catch (RetsException e)
        {
            LogUtil.error(e.getMessage(), e);
        }
    }

    /**
     * This method needs to be synchronized as there is a possibility of session being initialized by multiple threads.
     *
     * @return
     */
    protected synchronized RetsSession getSession()
    {
        if (session == null)
        {
            initializeSession();
        }

        return session;
    }


}
