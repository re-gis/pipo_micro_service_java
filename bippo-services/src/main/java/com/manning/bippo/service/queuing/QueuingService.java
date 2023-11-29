package com.manning.bippo.service.queuing;

import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.pojo.NtreisProperty;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;

public interface QueuingService
{

    void queuePhotoDownloadSinglePage(PropertySearchFilter propertySearchFilter, int page);

    @Async
    void queuePhotoDownload(PropertySearchFilter propertySearchFilter, int page);

    @Async
    void queuePhotoDownload(List<NtreisProperty> content, boolean alsoQueueFront);


    @Async
    void queueDownloadLatLong(int page, int size);

    @Async
    void queueDownloadLatLong(List<Long> content);

    @Async
    void queueDownloadLeaseProperties(List<NtreisProperty> content);

    @Async
    void queueSearchPropertiesProcesses(List<NtreisProperty> content, boolean forceRun);

    @Async
    void queueARVComps(List<NtreisProperty> list);

    @Async
    void queueUpdateRedisCache(Date startTime, Date endTime, boolean repairLatLong);

    @Async
    void queueUpdateRedisCacheTrestle(String region, Date startTime, Date endTime, boolean repairLatLong);
}
