package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.County;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MLSCountyRepository extends JpaRepository<County, Long>
{

    County findByValue(String value);

    List<County> findByInvalidOrderByValueAsc(boolean invalid);
}
