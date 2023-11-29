package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "mls_property_sub_type")
public class MLSPropertySubType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String value;
    String retsShortValue;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getRetsShortValue()
    {
        return retsShortValue;
    }

    public void setRetsShortValue(String retsShortValue)
    {
        this.retsShortValue = retsShortValue;
    }
}
