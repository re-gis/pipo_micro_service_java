package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.ZillowProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ZillowPropertyRepository extends JpaRepository<ZillowProperty, Long> {

    @Query(value = "SELECT * FROM zillow_property WHERE semantics_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public ZillowProperty findFirstBySemanticsId(int id);

    public ZillowProperty findFirstByFirstLineAndCityAndState(String firstLine, String city, String state);
}
