package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.OnBoardInformaticsMortgageOwnerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnBoardInformaticsMortgageOwnerResponseRepository extends JpaRepository<OnBoardInformaticsMortgageOwnerResponse, Long>
{

    OnBoardInformaticsMortgageOwnerResponse findFirstByAttomId(Long attomId);
}
