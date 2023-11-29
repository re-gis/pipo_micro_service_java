package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.TrestlePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrestlePhotoRepository extends JpaRepository<TrestlePhoto, Long>
{
    List<TrestlePhoto> findByListingKey(Integer listingKey);

    TrestlePhoto findFirstByTrestleUrl(String trestleUrl);

    TrestlePhoto findFirstByS3Url(String s3Url);
}
