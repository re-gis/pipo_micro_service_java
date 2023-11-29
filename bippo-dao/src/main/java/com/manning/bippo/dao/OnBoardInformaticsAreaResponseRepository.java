package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.OnBoardInformaticsAreaResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnBoardInformaticsAreaResponseRepository extends JpaRepository<OnBoardInformaticsAreaResponse, Long>
{

    OnBoardInformaticsAreaResponse findFirstByAreaCode(String areaCode);
}
