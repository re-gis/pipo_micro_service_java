package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.TrestleFrontPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrestleFrontPhotoRepository extends JpaRepository<TrestleFrontPhoto, Long>
{
    TrestleFrontPhoto findFirstByListingKey(Integer listingKey);

    TrestleFrontPhoto findFirstByTrestleUrl(String trestleUrl);

    TrestleFrontPhoto findFirstByS3Url(String s3Url);
}
