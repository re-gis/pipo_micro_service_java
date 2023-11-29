package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.NtreisPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NtreisPhotoRepository extends JpaRepository<NtreisPhoto, Long>
{
    @Query("SELECT a FROM NtreisPhoto a WHERE a.ntreisProperty.mLSNumber = :mlsNumber")
    List<NtreisPhoto> findByMLSNumber(@Param("mlsNumber") int mlsNumber);

    NtreisPhoto findFirstByS3Url(String s3Url);
}
