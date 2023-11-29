package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.RealtyTracAuction;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtyTracAuctionRepository extends JpaRepository<RealtyTracAuction, Long>
{
    /*
     * TODO: This Repository will eventually need to be updated to pass SitusState
     */
    Page<RealtyTracAuction> findBySitusCounty(String SitusCounty, Pageable pageable);

    @Query(value = "SELECT * FROM realty_trac_auction WHERE SitusCounty = ?1 AND RecordedAuctionDate >= ?2", nativeQuery = true)
    List<RealtyTracAuction> findBySitusCountyAndRecordedAuctionDateAfter(String SitusCounty, Date RecordedAuctionDate);

    @Query(value = "SELECT semantics_id FROM realty_trac_auction WHERE semantics_id is not null AND SitusCounty = ?1 AND RecordedAuctionDate >= ?2", nativeQuery = true)
    List<Integer> findSemanticsIdsBySitusCountyAndRecordedAuctionDateAfter(String SitusCounty, Date RecordedAuctionDate);

    @Query(value = "SELECT * FROM realty_trac_auction WHERE semantics_id is not null AND SitusCounty = ?1 AND RecordedAuctionDate >= ?2", nativeQuery = true)
    List<RealtyTracAuction> findSearchResultsBySitusCountyAndRecordedAuctionDateAfter(String SitusCounty, Date RecordedAuctionDate);

    @Query(value = "SELECT SitusCounty FROM realty_trac_auction GROUP BY SitusCounty ORDER BY count(1) DESC", nativeQuery = true)
    List<String> findCountiesDistinct();

    @Query(value = "SELECT SitusCounty FROM realty_trac_auction WHERE RecordedAuctionDate >= ?1 GROUP BY SitusCounty ORDER BY count(1) DESC", nativeQuery = true)
    List<String> findCountiesDistinctWithAuctionsAfter(Date RecordedAuctionDate);

    @Query(value = "SELECT count(1) FROM realty_trac_auction WHERE SitusCounty = ?1 AND RecordedAuctionDate >= ?2", nativeQuery = true)
    long countBySitusCountyAndRecordedAuctionDateAfter(String SitusCounty, Date RecordedAuctionDate);

    @Query(value = "SELECT min(RecordedAuctionDate) FROM realty_trac_auction WHERE SitusCounty = ?1 AND RecordedAuctionDate >= ?2", nativeQuery = true)
    Date findNextRecordedAuctionDateBySitusCountyAndRecordedAuctionDateAfter(String SitusCounty, Date RecordedAuctionDate);

    RealtyTracAuction findTopBySemanticsIdOrderByIdDesc(Long semanticsId);
}
