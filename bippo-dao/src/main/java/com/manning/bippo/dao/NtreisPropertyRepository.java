package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.MLSStatus;
import com.manning.bippo.dao.pojo.NtreisProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface NtreisPropertyRepository extends JpaRepository<NtreisProperty, Long>, JpaSpecificationExecutor<NtreisProperty>
{

    NtreisProperty findByMLSNumber(Integer MLSNumber);

    List<NtreisProperty> findByIdIn(List<Long> ids);

    List<NtreisProperty> findByMLSNumberIn(List<Integer> MLSNumbers);

    @Query(value = "select np.id from NtreisProperty np where np.latitude is null order by np.statusChangeTimestamp DESC")
    Page<Long> getPropertyIdsWithNoLatLong(Pageable pageable);

    @Query(value = "select np.matrix_Unique_ID from NtreisProperty np where np.status = :status order by np.updated DESC")
    Page<Integer> getMatrixUniqueIdsByStatus(@Param("status") String status, Pageable pageable);

    @Query(value = "select count(*) from ntreis as p" +
            " join geo_spatial_property as g on ntreis_id = p.id" +
            " where SqFtTotal >= :sqftTotalFrom" +
            " AND SqFtTotal <= :sqftTotalTo" +
            " AND YearBuilt >= :yearBuiltFrom" +
            " AND YearBuilt <= :yearBuiltTo" +
            " AND Status = :status" +
            " AND PropertySubType in (:propertySubType)" +
            " AND StatusChangeTimestamp >= :statusChangeTimestamp" +
            " AND MBRContains(LineString(Point(:longitude + :proximityInMiles / ( 69.0 / COS(RADIANS(:latitude))), :latitude + :proximityInMiles / 69.0)," +
            " Point(:longitude - :proximityInMiles / ( 69.0 / COS(RADIANS(:latitude))), :latitude - :proximityInMiles / 69.0)), g.latLong)" +
            " ",
            nativeQuery = true)
    Long countByCompsFilter(@Param("proximityInMiles") Double proximityInMiles,
                                           @Param("sqftTotalFrom") Integer sqftTotalFrom, @Param("sqftTotalTo") Integer sqftTotalTo,
                                           @Param("yearBuiltFrom") Integer yearBuiltFrom, @Param("yearBuiltTo") Integer yearBuiltTo,
                                           @Param("status") String status, @Param("propertySubType") List<String> propertySubType,
                                           @Param(("statusChangeTimestamp")) Date statusChangeTimestamp,
                                           @Param("longitude") Float longitude, @Param("latitude") Float latitude);

    @Query(value = "select p.* from ntreis as p" +
            " join geo_spatial_property as g on ntreis_id = p.id" +
            " where SqFtTotal >= :sqftTotalFrom" +
            " AND SqFtTotal <= :sqftTotalTo" +
            " AND YearBuilt >= :yearBuiltFrom" +
            " AND YearBuilt <= :yearBuiltTo" +
            " AND Status = :status" +
            " AND PropertySubType in (:propertySubType)" +
            " AND StatusChangeTimestamp >= :statusChangeTimestamp" +
            " AND MBRContains(LineString(Point(:longitude + :proximityInMiles / ( 69.0 / COS(RADIANS(:latitude))), :latitude + :proximityInMiles / 69.0)," +
            " Point(:longitude - :proximityInMiles / ( 69.0 / COS(RADIANS(:latitude))), :latitude - :proximityInMiles / 69.0)), g.latLong)" +
            " order by StatusChangeTimestamp DESC, LotSizeAreaSQFT DESC, ListPrice DESC",
            nativeQuery = true)
    List<NtreisProperty> findByCompsFilter(@Param("proximityInMiles") Double proximityInMiles,
                                           @Param("sqftTotalFrom") Integer sqftTotalFrom, @Param("sqftTotalTo") Integer sqftTotalTo,
                                           @Param("yearBuiltFrom") Integer yearBuiltFrom, @Param("yearBuiltTo") Integer yearBuiltTo,
                                           @Param("status") String status, @Param("propertySubType") List<String> propertySubType,
                                           @Param(("statusChangeTimestamp")) Date statusChangeTimestamp,
                                           @Param("longitude") Float longitude, @Param("latitude") Float latitude);

    @Query(value = "select p.* from ntreis as p" +
            " join geo_spatial_property as g on ntreis_id = p.id" +
            " where StatusChangeTimestamp >= :statusChangeTimestamp " +
            " and Status in (:statuses)" +
            " and MBRContains(LineString(Point(:longitude + :proximityInMiles / ( 69.0 / COS(RADIANS(:latitude))), :latitude + :proximityInMiles / 69.0)," +
            " Point(:longitude - :proximityInMiles / ( 69.0 / COS(RADIANS(:latitude))), :latitude - :proximityInMiles / 69.0)), g.latLong)",
            nativeQuery = true)
    List<NtreisProperty> findByDistanceAndRecency(@Param("proximityInMiles") Double proximityInMiles, @Param(("statusChangeTimestamp")) Date statusChangeTimestamp,
                                                  @Param("statuses") List<String> statuses, @Param("longitude") Float longitude, @Param("latitude") Float latitude);


    @Query(value = "select n.* from ntreis n" +
            " LEFT OUTER JOIN onboard_informatics_response o on n.id = o.ntreis_id" +
            " WHERE o.id IS NULL" +
            " order by n.StatusChangeTimestamp DESC" +
            " LIMIT 100",
            nativeQuery = true)
    List<NtreisProperty> findAllWithNoOIResponse();

    List<NtreisProperty> findByStatusChangeTimestampBetween(Date startTime, Date endTime);

    List<NtreisProperty> findByPropertyTypeAndStatusChangeTimestampBetween(String propertyType, Date startTime, Date endTime);

}
