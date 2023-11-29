package com.manning.bippo.aggregator.controller;

import com.manning.bippo.dao.MLSCityRepository;
import com.manning.bippo.dao.MLSCountyRepository;
import com.manning.bippo.dao.ZipCodeRepository;
import com.manning.bippo.dao.pojo.City;
import com.manning.bippo.dao.pojo.County;
import com.manning.bippo.dao.pojo.ZipCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.ArrayList;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class RegionalDataApiController
{
    private static final Pattern PATTERN_POSTAL_CODE = Pattern.compile("\\d{5}([ \\-]\\d{4})?");

    @Autowired
    ZipCodeRepository zipCodeRepository;

    @Autowired
    MLSCountyRepository mlsCountyRepository;

    @Autowired
    MLSCityRepository mlsCityRepository;

    @RequestMapping(value = "/search/metadata/county/{county}/zip-codes", method = RequestMethod.POST)
    public void addZipCodesToCounty(@PathVariable String county, @RequestParam List<Integer> zipCodes, @RequestParam String type)
    {
        County mlsCounty = mlsCountyRepository.findByValue(county);
        if (mlsCounty != null)
        {
            for (Integer zipCode : zipCodes)
            {
                ZipCode byCountyAndValue = zipCodeRepository.findByCountyAndValue(mlsCounty, zipCode);
                if (byCountyAndValue == null)
                {
                    byCountyAndValue = new ZipCode();
                    byCountyAndValue.setValue(zipCode);
                }
                byCountyAndValue.setCounty(mlsCounty);
                byCountyAndValue.setLocationType(type);
                zipCodeRepository.save(byCountyAndValue);
            }
        }

    }

    @RequestMapping(value = "/search/metadata/city/{city}/zip-codes", method = RequestMethod.POST)
    public void addZipCodesToCity(@PathVariable String city, @RequestParam List<Integer> zipCodes, @RequestParam String type)
    {
        City mlsCity = mlsCityRepository.findByValue(city);
        if (mlsCity != null)
        {
            for (Integer zipCode : zipCodes)
            {
                ZipCode byCountyAndValue = zipCodeRepository.findByCityAndValue(mlsCity, zipCode);
                if (byCountyAndValue == null)
                {
                    byCountyAndValue = new ZipCode();
                    byCountyAndValue.setValue(zipCode);
                    byCountyAndValue.setLocationType(type);
                }
                byCountyAndValue.setCity(mlsCity);
                byCountyAndValue.setLocationType(type);
                zipCodeRepository.save(byCountyAndValue);
            }
        }
    }

    @RequestMapping(value = "/data/zipcode/type", method = RequestMethod.POST)
    public ResponseEntity<String> setType(@RequestParam(required = false) String county,
            @RequestParam(required = false) String city, @RequestParam String type, @RequestParam List<String> zipcodes) {
        if (zipcodes.isEmpty()) {
            return new ResponseEntity<>("No zip codes specified!", HttpStatus.BAD_REQUEST);
        }

        final County mlsCounty;
        
        if (county == null) {
            mlsCounty = null;
        } else if ((mlsCounty = this.mlsCountyRepository.findByValue(county)) == null) {
            return new ResponseEntity<>("No county found by name " + county, HttpStatus.BAD_REQUEST);
        }

        final City mlsCity;

        if (city == null) {
            mlsCity = null;
        } else if ((mlsCity = this.mlsCityRepository.findByValue(city)) == null) {
            return new ResponseEntity<>("No city found by name " + city, HttpStatus.BAD_REQUEST);
        }

        final LocationType area;
        int updated = 0;

        {
            try {
                area = LocationType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("The area type must be one of urban, suburban, or rural.", HttpStatus.BAD_REQUEST);
            }

            for (String zipcode : zipcodes) {
                // Input validation for zipcodes: Note that this check only works for the US, though that should never be an issue
                if (RegionalDataApiController.PATTERN_POSTAL_CODE.matcher(zipcode).matches()) {
                    continue;
                } else if (zipcode.indexOf('-') >= 0) {
                    zipcode = zipcode.substring(0, zipcode.indexOf('-'));
                }

                final Integer value = Integer.valueOf(zipcode);
                ZipCode zl = this.zipCodeRepository.findByValue(value);

                if (zl == null) {
                    // No ZipCode was yet present
                    (zl = new ZipCode()).setValue(value);
                }

                // Only set the City / County if they were specified in the web request

                if (mlsCounty != null) {
                    zl.setCounty(mlsCounty);
                }

                if (mlsCity != null) {
                    zl.setCity(mlsCity);
                }

                zl.setLocationType(area.toString());
                this.zipCodeRepository.save(zl);
                updated++;
            }
        }

        if (updated < zipcodes.size()) {
            return new ResponseEntity<>("Failed to validate " + (zipcodes.size() - updated) + " zip codes", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully recorded " + zipcodes.size() + " zipcodes as " + area, HttpStatus.CREATED);
    }


    private static enum LocationType {

        URBAN, SUBURBAN, RURAL;

        public String toString() {
            return super.name().toLowerCase();
        }
    }
}
