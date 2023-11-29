package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyCompsFilterRepository extends JpaRepository<PropertyCompsFilter, Long>
{
    PropertyCompsFilter findTopBySubjectOrderByUpdated(NtreisProperty subjectProperty);

    PropertyCompsFilter findTopBySubjectAddressSemanticsOrderByUpdated(AddressSemantics addressSemantics);
}
