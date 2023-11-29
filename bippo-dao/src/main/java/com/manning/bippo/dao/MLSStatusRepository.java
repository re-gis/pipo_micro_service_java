package com.manning.bippo.dao;


import com.manning.bippo.dao.pojo.MLSStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MLSStatusRepository extends JpaRepository<MLSStatus, Long>
{
    MLSStatus findByValue(String value);

    List<MLSStatus> findByInvalidOrderByValueAsc(boolean invalid);
}
