package com.manning.bippo.dao.pojo;

import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.converter.CompsFilterCalculatedJsonConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "property_comps_filter")
@Data
public class PropertyCompsFilter
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "subject_id")
    NtreisProperty subject;

    @OneToOne
    @JoinColumn(name = "subject_address_semantics_id")
    AddressSemantics subjectAddressSemantics;

    @Convert(converter = CompsFilterCalculatedJsonConverter.class)
    @Column(name = "arv_filter_used")
    CompsFilterCalculated arvFilterUsed;

    @Convert(converter = CompsFilterCalculatedJsonConverter.class)
    @Column(name = "lease_filter_used")
    CompsFilterCalculated leaseFilterUsed;

    @Convert(converter = CompsFilterCalculatedJsonConverter.class)
    @Column(name = "area_filter_used")
    CompsFilterCalculated areaFilterUsed;

    @Column(name = "created")
    Date created;

    @Column(name = "updated")
    Date updated;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "propertyCompsFilter", orphanRemoval = true)
    Set<PropertyComp> propertyComps;

    @PrePersist
    protected void onCreate()
    {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updated = new Date();
    }

    public void setPropertyComps(Set<PropertyComp> propertyComps)
    {
        if (propertyComps == null)
        {
            this.propertyComps.clear();
        } else
        {
            if(this.propertyComps != null)
            {
                this.propertyComps.clear();
                this.propertyComps.addAll(propertyComps);
            }else
            {
                this.propertyComps = propertyComps;
            }
        }
    }
}
