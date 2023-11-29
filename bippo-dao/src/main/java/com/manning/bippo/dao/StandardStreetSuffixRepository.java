package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.StandardStreetSuffix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardStreetSuffixRepository extends JpaRepository<StandardStreetSuffix, Long>
{

    StandardStreetSuffix findFirstByKey(String key);

    int countByKey(String key);
    
    int countByValue(String value);

    default boolean isValidSuffix(String suffix) {
        return this.countByKey(suffix) > 0 || this.countByValue(suffix) > 0;
    }
}
