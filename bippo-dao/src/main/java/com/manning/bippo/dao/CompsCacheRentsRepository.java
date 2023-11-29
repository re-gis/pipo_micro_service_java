package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.CompsCacheRents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompsCacheRentsRepository extends JpaRepository<CompsCacheRents, Long> {

    @Query(value = "SELECT * FROM comps_cache_rents WHERE subject_semantics_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheRents findFirstBySemanticsId(int id);

    @Query(value = "SELECT * FROM comps_cache_rents WHERE subject_ntreis_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheRents findFirstByNtreisId(int id);

    @Query(value = "SELECT * FROM comps_cache_rents WHERE subject_tax_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheRents findFirstByTaxId(int id);

    @Query(value = "SELECT * FROM comps_cache_rents WHERE subject_auction_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheRents findFirstByAuctionId(int id);
}
