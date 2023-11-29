package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.TaxProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TaxPropertyRepository extends JpaRepository<TaxProperty, Long>
{
    @Query(value = "select p.* from new_property as p" +
            " join geo_new_property as g on g.property_id = p.id" +
            " where MBRContains(LineString(Point (:longitude + 0.2 / ( 111.1 / COS(RADIANS(:latitude))), :latitude + 0.2 / 111.1)," +
            "                       Point (:longitude - 0.2 / ( 111.1 / COS(RADIANS(:latitude))), :latitude - 0.2 / 111.1)), g.LatLong)" +
            "                       and p.FullAddress like CONCAT(:likePrefix, '%')", nativeQuery = true)
    List<TaxProperty> findByFullAddressLikeAndLatitudeAndLongitude(@Param("likePrefix") String likePrefix,
                                                                   @Param("latitude") BigDecimal latitude,
                                                                   @Param("longitude") BigDecimal longitude);
}
