package com.manning.bippo.dao.pojo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bpo")
public class BPO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "bpo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<BPOSoldComp> soldComps;

    @OneToOne
    @JoinColumn(name = "subject_id")
    NtreisProperty subject;

    @Column(name = "created")
    Date created;

    @Column(name = "updated")
    Date updated;

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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public List<BPOSoldComp> getSoldComps()
    {
        return soldComps;
    }

    public void setSoldComps(List<BPOSoldComp> soldComps)
    {
        this.soldComps = soldComps;
    }

    public NtreisProperty getSubject()
    {
        return subject;
    }

    public void setSubject(NtreisProperty subject)
    {
        this.subject = subject;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }
}
