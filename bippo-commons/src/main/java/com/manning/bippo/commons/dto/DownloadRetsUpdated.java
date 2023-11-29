package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.Date;

public class DownloadRetsUpdated implements Serializable
{

    private final Date startTime;
    private final Date endTime;

    public DownloadRetsUpdated(Date startTime, Date endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
}
