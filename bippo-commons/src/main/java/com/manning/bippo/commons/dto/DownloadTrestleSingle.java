package com.manning.bippo.commons.dto;

import java.io.Serializable;

public class DownloadTrestleSingle implements Serializable
{
    private final String originatingSystem;
    private final int listingId;

    public DownloadTrestleSingle(String originatingSystem, int listingId) {
        this.originatingSystem = originatingSystem;
        this.listingId = listingId;
    }

    public String getOriginatingSystem() {
        return originatingSystem;
    }

    public int getListingId() {
        return listingId;
    }
}
