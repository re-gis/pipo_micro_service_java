package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "property_comp")
public class PropertyComp
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "property_comps_filter_id", referencedColumnName = "id")
    PropertyCompsFilter propertyCompsFilter;

    @OneToOne
    @JoinColumn(name = "comp_id")
    NtreisProperty comp;

    String type;

    public PropertyComp()
    {
    }

    public PropertyComp(PropertyCompsFilter finalPropertyCompsFilter, NtreisProperty insertedProerty, String type)
    {
        this.propertyCompsFilter = finalPropertyCompsFilter;
        this.comp = insertedProerty;
        this.type = type;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public PropertyCompsFilter getPropertyCompsFilter()
    {
        return propertyCompsFilter;
    }

    public void setPropertyCompsFilter(PropertyCompsFilter propertyCompsFilter)
    {
        this.propertyCompsFilter = propertyCompsFilter;
    }

    public NtreisProperty getComp()
    {
        return comp;
    }

    public void setComp(NtreisProperty comp)
    {
        this.comp = comp;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
