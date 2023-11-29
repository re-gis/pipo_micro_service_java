package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.MLSPropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MLSPropertyTypeRepository extends JpaRepository<MLSPropertyType, Long>
{
    MLSPropertyType findByValue(String value);
}
