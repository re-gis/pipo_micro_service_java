package com.manning.bippo.dao;

import com.manning.bippo.dao.rets.pojo.RetsMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetsMetadataRepository extends JpaRepository<RetsMetadata, Long>
{
    RetsMetadata findByName(String name);
}
