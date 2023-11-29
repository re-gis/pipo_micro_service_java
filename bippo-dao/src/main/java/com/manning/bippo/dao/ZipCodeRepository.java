package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.City;
import com.manning.bippo.dao.pojo.County;
import com.manning.bippo.dao.pojo.ZipCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCode, Long>
{

    List<ZipCode> findByCounty(County county);

    List<ZipCode> findByCity(City city);

    ZipCode findByCountyAndValue(County county, Integer value);

    ZipCode findByCityAndValue(City city, Integer zipCode);
    
    ZipCode findByValue(Integer value);
}
