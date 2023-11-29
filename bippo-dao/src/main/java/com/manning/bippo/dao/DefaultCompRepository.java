package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.DefaultComp;
import com.manning.bippo.dao.pojo.NtreisProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultCompRepository extends JpaRepository<DefaultComp, Long>
{
    List<DefaultComp> findBySubject(NtreisProperty subject);

    DefaultComp findBySubjectAndComp(NtreisProperty subject, NtreisProperty comp);
}
