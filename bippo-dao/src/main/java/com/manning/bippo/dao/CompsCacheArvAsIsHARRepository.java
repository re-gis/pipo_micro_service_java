package com.manning.bippo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.manning.bippo.dao.pojo.CompsCacheArvAsIs;
import com.manning.bippo.dao.pojo.CompsCacheArvAsIsHAR;

@Repository
public interface CompsCacheArvAsIsHARRepository extends JpaRepository<CompsCacheArvAsIsHAR, Long> {

    @Query(value = "SELECT * FROM comps_cache_arv_as_is_har WHERE trestle_id = ?1 ORDER BY id DESC LIMIT 1",
           nativeQuery = true)
    public CompsCacheArvAsIsHAR findFirstByTrestleId(Long id);
}
