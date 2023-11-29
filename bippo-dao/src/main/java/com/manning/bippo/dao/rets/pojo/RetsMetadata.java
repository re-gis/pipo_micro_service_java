package com.manning.bippo.dao.rets.pojo;

import com.manning.bippo.dao.converter.RetsResourceConverter;

import javax.persistence.*;

@Entity
@Table(name = "rets_metadata")
public class RetsMetadata
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Convert(converter = RetsResourceConverter.class)
    RetsResource value;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public RetsResource getValue()
    {
        return value;
    }

    public void setValue(RetsResource value)
    {
        this.value = value;
    }
}
