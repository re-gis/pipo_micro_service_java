package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MLSCityRepository extends JpaRepository<City, Long>
{
    City findByValue(String value);
}
