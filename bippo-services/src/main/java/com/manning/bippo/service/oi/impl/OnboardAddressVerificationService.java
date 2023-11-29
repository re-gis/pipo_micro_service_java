package com.manning.bippo.service.oi.impl;

import com.manning.bippo.service.address.verifiy.pojo.OnboardAddressVerificationResponse;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;

@Service("onboard")
public class OnboardAddressVerificationService implements AddressVerificationService {

    @Autowired
    OnBoardInformaticsPropertyService onboardService;

    @Override
    public OnboardAddressVerificationResponse query(String address) throws IOException {
        final int split = address.indexOf(',');

        if (split <= 0) {
            throw new IllegalStateException("Cannot parse first line / last line from input address: " + address);
        }

        final AllEventsPropertyResponse resp = this.onboardService.getAllEvents(
                address.substring(0, split).trim(), address.substring(split + 1, address.length()).trim());

        if (resp == null || resp.property == null || resp.property.length < 1) {
            throw new IOException("Failed to query onboard.");
        }

        return new OnboardAddressVerificationResponse(resp.property[0]);
    }

    @Override
    public List<OnboardAddressVerificationResponse> queryMultiple(Collection<String> addresses) {
        final List<OnboardAddressVerificationResponse> responses = new ArrayList<>();

        for (String address : addresses) {
            try {
                responses.add(this.query(address));
            } catch (IOException e) {
                throw new IllegalStateException("Query failed.", e);
            }
        }

        return responses;
    }

    @Override
    public <K> Map<K, OnboardAddressVerificationResponse> queryMultipleMapped(Map<K, String> properties) {
        final Map<K, OnboardAddressVerificationResponse> responses = new HashMap<>();

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
