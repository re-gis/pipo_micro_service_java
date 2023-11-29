package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.AuctionProperty;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionPropertyRepository extends JpaRepository<AuctionProperty, Long>
{
    @Query(value = "SELECT county FROM auctions GROUP BY county ORDER BY count(1) DESC", nativeQuery = true)
    List<String> findCountiesDistinct();

    Long countByCountyAndAuctionGreaterThanEqual(String county, Date auction);

    @Query(value = "SELECT auction FROM auctions WHERE county = ?1 AND auction >= ?2", nativeQuery = true)
    List<Date> findDatesByCountyAndAuctionGreaterThanEqual(String county, Date auction);

    List<AuctionProperty> findByCountyAndAuctionGreaterThanEqual(String county, Date auction);

    @Query(value = "SELECT * FROM auctions WHERE county = ?1 AND auction >= ?2 AND auction <= ?3", nativeQuery = true)
    List<AuctionProperty> findByCountyAndAuctionBetween(String county, Date minDate, Date maxDate);

    @Query(value = "SELECT id FROM auctions WHERE county = ?1 AND auction >= ?2 AND auction <= ?3", nativeQuery = true)
    List<Integer> findIdsByCountyAndAuctionBetween(String county, Date minDate, Date maxDate);
}
