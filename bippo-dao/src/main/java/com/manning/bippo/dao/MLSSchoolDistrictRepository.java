package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.MLSSchoolDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MLSSchoolDistrictRepository extends JpaRepository<MLSSchoolDistrict, Long>
{
    MLSSchoolDistrict findByValue(String value);
}
