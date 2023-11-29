package com.manning.bippo.service.mapping.pojo;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Basic POD type for JSON deserialization of Trestle data.
 */
public class TrestleApiData {

    @SerializedName("@odata.context")
    public String context;
    @SerializedName("@odata.nextLink")
    public String nextLink;
    public JsonObject[] value;
    public transient int[] keys;

    public boolean hasNextPage() {
        return !Strings.isNullOrEmpty(this.nextLink);
    }

    public boolean validate() {
        if (this.value == null || this.value.length < 1) {
            return false;
        }

        this.keys = new int[this.value.length];

        for (int i = 0; i < this.value.length; i++) {
            this.keys[i] = this.value[i].get("ListingKeyNumeric").getAsInt();
        }

        return true;
    }

    public String getSanitizedNextLink() {
        if (this.nextLink == null || this.nextLink.indexOf(' ') < 0) {
            return this.nextLink;
        }

        return this.nextLink.replace(" ", "%20");
    }
}
