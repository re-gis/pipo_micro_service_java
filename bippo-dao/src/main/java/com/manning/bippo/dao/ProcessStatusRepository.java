package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessStatusRepository extends JpaRepository<ProcessStatus, Long>
{
    ProcessStatus findByProcessName(String process);
}
