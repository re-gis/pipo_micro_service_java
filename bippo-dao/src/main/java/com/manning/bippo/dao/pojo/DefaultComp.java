package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "default_comp")
public class DefaultComp
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    NtreisProperty subject;

    @ManyToOne
    @JoinColumn(name = "comp_id", referencedColumnName = "id")
    NtreisProperty comp;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public NtreisProperty getSubject()
    {
        return subject;
    }

    public void setSubject(NtreisProperty subject)
    {
        this.subject = subject;
    }

    public NtreisProperty getComp()
    {
        return comp;
    }

    public void setComp(NtreisProperty comp)
    {
        this.comp = comp;
    }
}
