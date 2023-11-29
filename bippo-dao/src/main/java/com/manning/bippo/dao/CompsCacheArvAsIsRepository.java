package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.CompsCacheArvAsIs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompsCacheArvAsIsRepository extends JpaRepository<CompsCacheArvAsIs, Long> {

    @Query(value = "SELECT * FROM comps_cache_arv_as_is WHERE subject_semantics_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheArvAsIs findFirstBySemanticsId(int id);

    @Query(value = "SELECT * FROM comps_cache_arv_as_is WHERE subject_ntreis_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheArvAsIs findFirstByNtreisId(int id);

    @Query(value = "SELECT * FROM comps_cache_arv_as_is WHERE subject_tax_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheArvAsIs findFirstByTaxId(int id);

    @Query(value = "SELECT * FROM comps_cache_arv_as_is WHERE subject_auction_id = ?1 ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    public CompsCacheArvAsIs findFirstByAuctionId(int id);
}
