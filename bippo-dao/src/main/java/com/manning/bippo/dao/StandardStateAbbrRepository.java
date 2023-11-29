package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.StandardStateAbbr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardStateAbbrRepository extends JpaRepository<StandardStateAbbr, Long>
{

    StandardStateAbbr findFirstByKey(String key);

    int countByKey(String key);

    int countByValue(String value);

    default boolean isValidState(String state) {
        return this.countByKey(state) > 0 || this.countByValue(state) > 0;
    }
}
