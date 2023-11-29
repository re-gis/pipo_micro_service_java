package com.manning.bippo.service.auction;

import com.google.common.base.Strings;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.RealtyTracAuctionRepository;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.RealtyTracAuction;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealtyTracAuctionService {

    private static final SimpleDateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat US_DATE_TIME = new SimpleDateFormat("MMM d yyyy hh:mmaaa");

    @Autowired
    RealtyTracAuctionRepository realtyTracAuctionRepository;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    public boolean insertAuction(String[] key, String[] values) {
        final int len = Math.min(key.length, values.length);
        final Map<String, String> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            map.put(key[i], values[i]);
        }

        final RealtyTracAuction auction = new RealtyTracAuction();

        for (Field f : RealtyTracAuction.class.getDeclaredFields()) {
            final String v = map.get(f.getName());

            if (!Strings.isNullOrEmpty(v)) {
                f.setAccessible(true);

                try {
                    if (f.getType().isAssignableFrom(String.class)) {
                        f.set(auction, v);
                    } else if (f.getType().isAssignableFrom(Integer.class)) {
                        f.set(auction, Integer.valueOf(v));
                    } else if (f.getType().isAssignableFrom(Double.class)) {
                        f.set(auction, Double.valueOf(v));
                    } else if (f.getType().isAssignableFrom(Float.class)) {
                        f.set(auction, Float.valueOf(v));
                    } else if (f.getType().isAssignableFrom(Date.class)) {
                        Date d;

                        try {
                            // A majority of dates are given in ISO date format (no time)
                            d = RealtyTracAuctionService.ISO_DATE.parse(v);
                        } catch (ParseException e) {
                            // A few fringe cases are defined in a US format
                            d = RealtyTracAuctionService.US_DATE_TIME.parse(v);
                        }

                        f.set(auction, DateUtils.addSeconds(d, 86399));
                    }
                } catch (NumberFormatException | ParseException e) {
                    LogUtil.error("Failed to parse value '{}' for RealtyTracAuction field {}.", v, f.getName());
                } catch (Exception e) {
                    LogUtil.error("Failed to map " + f.getName() + " in RealtyTracAuction.", e);
                }
            }
        }

        if (auction.hasStandardizableAddress()) {
            try {
                final AddressSemantics as = this.addressSemanticizationService.forceSemanticize(new RealtyTracAddressWrapper(auction), null);

                if (as != null) {
                    auction.setSemanticsId(as.getId());
                }
            } catch (Exception e) {
                LogUtil.error("Failed to load AddressSemantics to link with RealtyTracAuction", e);
            }
        }

        try {
            this.realtyTracAuctionRepository.save(auction);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void translateKeys(String[] a) {
        final int len = a.length;

        for (int i = 0; i < len; i++) {
            final String s = a[i];

            if (s.indexOf(' ') < 1 && s.indexOf('/') < 0) {
                continue;
            }

            a[i] = s.replaceAll(" |/", "_");
        }
    }
}
