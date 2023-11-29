package com.manning.bippo.dao.pojo;

import com.manning.bippo.dao.converter.AdjustmentJsonConverter;

import javax.persistence.*;

@Entity
@Table(name = "bpo_sold_comps")
public class BPOSoldComp
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "ntreis_id")
    NtreisProperty ntreisProperty;

    @ManyToOne
    @JoinColumn(name = "bpo_id")
    BPO bpo;

    @Convert(converter = AdjustmentJsonConverter.class)
    Adjustment adjustment;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public NtreisProperty getNtreisProperty()
    {
        return ntreisProperty;
    }

    public void setNtreisProperty(NtreisProperty ntreisProperty)
    {
        this.ntreisProperty = ntreisProperty;
    }

    public BPO getBpo()
    {
        return bpo;
    }

    public void setBpo(BPO bpo)
    {
        this.bpo = bpo;
    }

    public Adjustment getAdjustment()
    {
        return adjustment;
    }

    public void setAdjustment(Adjustment adjustment)
    {
        this.adjustment = adjustment;
    }
}
