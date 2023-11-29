package com.manning.bippo.service.geocode;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import com.manning.bippo.dao.GeoRooftopRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.GeoRooftop;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address.verifiy.pojo.GoogleAddressVerificationResponse;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.address_semanticize.pojo.SemanticsIds;
import com.manning.bippo.service.geocode.impl.GoogleGeocodeServiceImpl;
import com.manning.bippo.service.geocode.pojo.GeocodeResponse;
import com.manning.bippo.service.profiling.ProfilingMetricsService;
import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RooftopLatLongService {

    @Autowired
    GoogleGeocodeServiceImpl googleGeocodingService;

    @Autowired
    GeoRooftopRepository rooftopRepository;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    @Autowired
    ProfilingMetricsService profilingMetricsService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    private void insertOrUpdateLatLong(AddressSemantics property, double lat, double lon) {
        GeoRooftop row = this.rooftopRepository.findFirstBySemanticsId(property.getId().intValue());

        if (row == null) {
            // Create a new row, if not present
            row = new GeoRooftop();
            row.setSemantics(property);
        }

        // Update the lat/long and creation date of the new or existing row
        row.setLatitude(Double.valueOf(lat));
        row.setLongitude(Double.valueOf(lon));
        row.setCreationDate(new Date());

        this.rooftopRepository.save(row);
    }

    public LatLongCoordinates forceLatLong(AddressSemantics property) {
        if (property == null) {
            throw new IllegalArgumentException("Given AddressSemantics property was null");
        }

        final GeocodeResponse resp = this.googleGeocodingService.geocodeOneLineAddress(property.getFirstLine() + " " + property.getLastLine());
        LatLongCoordinates ll = null;

        try {
            ll = resp.requireSingle();
        } catch (IllegalStateException e) {
            // There were many results, we'll just pick the first one for now
            LogUtil.warn(property.getFirstLine() + " " + property.getLastLine() + " yielded many results: " + resp.toString());
            ll = resp.requireAny();
        } catch (NoSuchElementException e) {
            // There were no results
            LogUtil.error(property.getFirstLine() + " " + property.getLastLine() + " yielded no results.");
        }

        if (ll == null) {
            throw new IllegalStateException("Failed to lookup LatLong!");
        }

        this.insertOrUpdateLatLong(property, ll.getLatitude(), ll.getLongitude());
        return ll;
    }

    public LatLongCoordinates tryLatLong(AddressSemantics property) {
        if (property == null) {
            return null;
        }

        final GeoRooftop row = this.rooftopRepository.findFirstBySemanticsId(property.getId().intValue());
        return row == null ? null : new LatLongCoordinates(row.getLatitude(), row.getLongitude());
    }

    public LatLongCoordinates getLatLong(AddressSemantics property) {
        if (property == null) {
            throw new IllegalArgumentException("Given AddressSemantics property was null");
        }

        final GeoRooftop row = this.rooftopRepository.findFirstBySemanticsId(property.getId().intValue());

        if (row == null || row.getCreationDate() == null || row.getCreationDate().before(DateUtils.addDays(new Date(), -30))) {
            try {// If the creation date is 30 days old (or is somehow null), recache the lat/long
                return this.forceLatLong(property);
            } catch (Exception e) {
                // If we have failed for some reason, but we have a cached result, use that instead
                if (row != null) {
                    return new LatLongCoordinates(row.getLatitude(), row.getLongitude());
                }

                throw e;
            }
        }

        // Otherwise, load and return the existing lat/long
        return new LatLongCoordinates(row.getLatitude(), row.getLongitude());
    }

    public LatLongCoordinates getLatLong(NtreisProperty property) {
        return this.getLatLong(this.addressSemanticizationService.findTopByNtreisProperty(property));
    }

    public boolean updateFromAddressVerification(AddressSemantics property, AddressVerificationResponse avr) {
        if (property != null && avr.hasLatLong() && avr instanceof GoogleAddressVerificationResponse && avr.getLatitude() > 0d && avr.getLongitude() > 0d) {
            // We only want to trust AVR's from GoogleGeocodeServiceImpl, as we know they are more likely to be good quality
            this.insertOrUpdateLatLong(property, avr.getLatitude(), avr.getLongitude());
            return true;
        }

        return false;
    }

    public boolean updateFromAddressVerification(NtreisProperty property, AddressVerificationResponse avr) {
        return property == null ? false : this.updateFromAddressVerification(this.addressSemanticizationService.findTopByNtreisProperty(property), avr);
    }

    public NtreisProperty propagateLatLong(NtreisProperty to) {
        AddressSemantics as = this.addressSemanticizationService.findTopByNtreisProperty(to);
        
        if (as == null) {
            try {
                this.profilingMetricsService.incrementCounter("ss_RooftopLatLongService_propagateLatLong");
                as = this.addressSemanticizationService.semanticize(to, SemanticsIds.builder().ntreisId(to.getId()).build());
            } catch (IOException e) {
                LogUtil.debug("Failed to propagate Lat/Long to " + to.getAddress() + ": could not semanticize", e);
                throw new IllegalStateException("Failed to semanticize", e);
            }
        }
        
        final LatLongCoordinates ll;
        
        try {
            ll = this.getLatLong(as);
        } catch (Exception e) {
            LogUtil.debug("Failed to propagate Lat/Long to " + to.getAddress(), e);
            throw e;
        }
        
        to.setLatitude(ll.getLatitude().floatValue());
        to.setLongitude(ll.getLongitude().floatValue());
        LogUtil.debug("Propagating Lat/Long {}/{} to {}", ll.getLatitude(), ll.getLongitude(), to.getAddress());
        return this.ntreisPropertyRepository.save(to);
    }

    public NtreisProperty tryPropagateLatLong(NtreisProperty to) {
        AddressSemantics as = this.addressSemanticizationService.findTopByNtreisProperty(to);

        if (as == null) {
            return null;
        }

        final LatLongCoordinates ll = this.tryLatLong(as);

        if (ll == null) {
            return null;
        }

        to.setLatitude(ll.getLatitude().floatValue());
        to.setLongitude(ll.getLongitude().floatValue());
        LogUtil.debug("Propagating Lat/Long {}/{} to {}", ll.getLatitude(), ll.getLongitude(), to.getAddress());
        return this.ntreisPropertyRepository.save(to);
    }
}
