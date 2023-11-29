package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.OnBoardInformaticsAnalyticsResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnBoardInformaticsAnalyticsResponseRepository extends JpaRepository<OnBoardInformaticsAnalyticsResponse, Long>
{
    OnBoardInformaticsAnalyticsResponse findTopByZipAndYearAndQuarter(int zip, int year, int quarter);

    List<OnBoardInformaticsAnalyticsResponse> findByZipAndYearOrderByQuarterAsc(int zip, int year);
    List<OnBoardInformaticsAnalyticsResponse> findByZipAndYearBetweenOrderByYearAscQuarterAsc(int zip, int yearLow, int yearHigh);
}
