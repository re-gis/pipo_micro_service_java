package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.CompsCacheWholesale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompsCacheWholesaleRepository extends JpaRepository<CompsCacheWholesale, Long> {

    @Query(value = "SELECT * FROM comps_cache_wholesale WHERE subject_semantics_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheWholesale findFirstBySemanticsId(int id);

    @Query(value = "SELECT * FROM comps_cache_wholesale WHERE subject_ntreis_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheWholesale findFirstByNtreisId(int id);

    @Query(value = "SELECT * FROM comps_cache_wholesale WHERE subject_tax_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheWholesale findFirstByTaxId(int id);

    @Query(value = "SELECT * FROM comps_cache_wholesale WHERE subject_auction_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheWholesale findFirstByAuctionId(int id);
}
