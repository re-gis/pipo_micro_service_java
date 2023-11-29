package com.manning.bippo.service.trigger.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.ProcessStatusRepository;
import com.manning.bippo.dao.TaxPropertyRepository;
import com.manning.bippo.dao.pojo.ProcessStatus;
import com.manning.bippo.dao.pojo.TaxProperty;
import com.manning.bippo.service.trigger.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Comparator;

public abstract class AbstractTriggerService implements TriggerService
{
    public static final int RETURN_COUNT_SIZE = 100;

    @Autowired
    TaxPropertyRepository taxPropertyRepository;

    @Autowired
    ProcessStatusRepository processStatusRepository;

    public int getSkipPageNumber()
    {
        ProcessStatus retsFeedProcessStatus = processStatusRepository.findByProcessName(getProcessName());
        if (retsFeedProcessStatus == null)
        {
            ProcessStatus entityToSave = new ProcessStatus();
            entityToSave.setProcessName(getProcessName());
            entityToSave.setIdSkip(0L);

            retsFeedProcessStatus = processStatusRepository.save(entityToSave);
        }

        return (int) (retsFeedProcessStatus.getIdSkip() / RETURN_COUNT_SIZE);
    }

    public synchronized void updateProcessStatus(long idSkip)
    {
        ProcessStatus retsFeedProcessStatus = processStatusRepository.findByProcessName(getProcessName());
        if (retsFeedProcessStatus == null)
        {
            retsFeedProcessStatus = new ProcessStatus();
            retsFeedProcessStatus.setProcessName(getProcessName());
        }
        retsFeedProcessStatus.setIdSkip(idSkip);
        processStatusRepository.save(retsFeedProcessStatus);
    }

    /**
     * Should never throw exception.
     *
     * @param properties
     */
    public void updateProcessStatusIdSkip(Page<TaxProperty> properties)
    {
        try
        {
            Long skipToId = properties.getContent().parallelStream().max(new Comparator<TaxProperty>()
            {
                @Override
                public int compare(TaxProperty o1, TaxProperty o2)
                {
                    return o1.getId().compareTo(o2.getId());
                }
            }).get().getId();
            updateProcessStatus(skipToId);
        }catch (Exception e)
        {
            LogUtil.error(e.getMessage(), e);
        }
    }

}
