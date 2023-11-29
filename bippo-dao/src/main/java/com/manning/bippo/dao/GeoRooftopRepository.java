package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.GeoRooftop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoRooftopRepository extends JpaRepository<GeoRooftop, Long> {

    @Query(value = "SELECT * FROM geo_rooftop WHERE semantics_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public GeoRooftop findFirstBySemanticsId(int id);
}
