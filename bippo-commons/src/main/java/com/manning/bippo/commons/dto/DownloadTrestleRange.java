package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.Date;

public class DownloadTrestleRange implements Serializable
{
    private final String originatingSystem;
    private final Date startTime;
    private final Date endTime;

    public DownloadTrestleRange(String originatingSystem, Date startTime, Date endTime)
    {
        this.originatingSystem = originatingSystem;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getOriginatingSystem()
    {
        return originatingSystem;
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
