package com.manning.bippo.service.address.standardize;

import com.manning.bippo.dao.StandardStateAbbrRepository;
import com.manning.bippo.dao.StandardStreetSuffixRepository;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.StandardStateAbbr;
import com.manning.bippo.dao.pojo.StandardStreetSuffix;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressStandardizationService {

    @Autowired
    StandardStreetSuffixRepository suffixRepository;

    @Autowired
    StandardStateAbbrRepository stateRepository;

    public AddressVerificationResponse standardizeAbstractProperty(AbstractProperty prop) {
        if (!prop.hasStandardizableAddress()) {
            return null;
        } else if (!this.suffixRepository.isValidSuffix(prop.getStreetSuffix()) || !this.stateRepository.isValidState(prop.getStateOrProvince())) {
            return null;
        }

        final String number = prop.getStreetNumber(), street = prop.getStreetName(), prefixDir = prop.getStreetDirPrefix(),
                suffixDir = prop.getStreetDirSuffix(), city = prop.getCity(), zip = prop.getPostalCode();
        String suffix = prop.getStreetSuffix(), state = prop.getStateOrProvince();

        final StandardStreetSuffix standardizedSuffix = this.suffixRepository.findFirstByKey(suffix);
        final StandardStateAbbr standardizedState = this.stateRepository.findFirstByKey(state);

        if (standardizedSuffix != null) {
            // If standardizedSuffix is null, then the given suffix is already a value of the table (an accepted suffix notation)
            suffix = standardizedSuffix.getValue();
        }

        if (standardizedState != null) {
            // If standardizedState is null, then the given state is already a value of the table (an accepted abbreviation)
            state = standardizedState.getValue();
        }
        
        return new StandardizedAddress(number, prefixDir, street, suffix, suffixDir, city, state, zip);
    }

    private static final class StandardizedAddress implements AddressVerificationResponse {

        private final String number, street, suffix, city, state, zip;
        private final String firstLine, lastLine, oneLine;

        StandardizedAddress(String number, String pdir, String street, String suffix, String sdir, String city, String state, String zip) {
            this.number = number;
            this.street = street;
            this.suffix = suffix;
            this.city = city;
            this.state = state;
            this.zip = zip;

            final StringBuilder firstLine = new StringBuilder(number).append(' ');

            if (pdir != null && !pdir.isEmpty()) {
                firstLine.append(Character.toUpperCase(pdir.charAt(0))).append(' ');
            }

            firstLine.append(street).append(' ').append(suffix);

            if (sdir != null && !sdir.isEmpty()) {
                firstLine.append(Character.toUpperCase(sdir.charAt(0))).append(' ');
            }

            this.firstLine = firstLine.toString();
            this.lastLine = String.format("%s %s %s", city, state, zip);
            this.oneLine = String.format("%s, %s", this.firstLine, this.lastLine);
        }

        @Override
        public String getFullAddress() {
            return this.oneLine;
        }

        @Override
        public String getFirstLine() {
            return this.firstLine;
        }

        @Override
        public String getLastLine() {
            return this.lastLine;
        }

        @Override
        public boolean hasLatLong() {
            return false;
        }

        @Override
        public double getLatitude() {
            return Double.NaN;
        }

        @Override
        public double getLongitude() {
            return Double.NaN;
        }

        @Override
        public boolean hasAddressParts() {
            return true;
        }

        @Override
        public String getStreetNumber() {
            return this.number;
        }

        @Override
        public String getStreetName() {
            return this.street;
        }

        @Override
        public String getStreetSuffix() {
            return this.suffix;
        }

        @Override
        public String getCity() {
            return this.city;
        }

        @Override
        public String getState() {
            return this.state;
        }

        @Override
        public int getZipCode() {
            return Integer.parseInt(this.zip);
        }
    }
}
