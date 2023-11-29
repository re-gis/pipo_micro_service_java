package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.MLSPropertySubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MLSPropertySubTypeRepository extends JpaRepository<MLSPropertySubType, Long>
{
    MLSPropertySubType findByValue(String value);
}
