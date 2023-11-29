package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.OnBoardInformaticsPoiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnBoardInformaticsPoiResponseRepository extends JpaRepository<OnBoardInformaticsPoiResponse, Long>
{

    OnBoardInformaticsPoiResponse findFirstByAddress(String address);
}
