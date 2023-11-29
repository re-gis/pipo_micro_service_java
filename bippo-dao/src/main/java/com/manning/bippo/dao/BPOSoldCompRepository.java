package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.BPOSoldComp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BPOSoldCompRepository extends JpaRepository<BPOSoldComp, Long>
{
}
