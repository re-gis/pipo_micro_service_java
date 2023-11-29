package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "mls_status")
public class MLSStatus
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String value;
    String retsShortValue;
    boolean invalid;

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

    public boolean isInvalid()
    {
        return invalid;
    }

    public void setInvalid(boolean invalid)
    {
        this.invalid = invalid;
    }
}
