package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.OnBoardInformaticsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnBoardInformaticsResponseRepository extends JpaRepository<OnBoardInformaticsResponse, Long>
{

    OnBoardInformaticsResponse findFirstByNtreisPropertyOrderByCreatedDesc(NtreisProperty ntreisProperty);

    @Query(value = "select np from OnBoardInformaticsResponse np where np.response like :obPropId order by np.created")
    List<OnBoardInformaticsResponse> findByObPropId(@Param("obPropId") String obPropId);
}
