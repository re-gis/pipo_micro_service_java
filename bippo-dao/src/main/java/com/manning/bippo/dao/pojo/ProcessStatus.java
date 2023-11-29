package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "process_status")
public class ProcessStatus
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "process_name")
    String processName;

    @Column(name = "id_skip")
    Long idSkip;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getProcessName()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName = processName;
    }

    public Long getIdSkip()
    {
        return idSkip;
    }

    public void setIdSkip(Long idSkip)
    {
        this.idSkip = idSkip;
    }
}
