package com.manning.bippo.service;

import com.manning.bippo.dao.ZipCodeRepository;
import com.manning.bippo.dao.pojo.ZipCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZipCodeService
{

    @Autowired
    ZipCodeRepository zipCodeRepository;

    public String getLocationType(String postalcode)
    {
        final Integer value;

        try
        {
            value = Integer.valueOf(postalcode);
        } catch (NumberFormatException e)
        {
            return "";
        }

        final ZipCode zipcode = this.zipCodeRepository.findByValue(value);
        final String type;
        return zipcode == null || (type = zipcode.getLocationType()) == null ? "" : type;
    }
}
