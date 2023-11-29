package com.manning.bippo.service.trigger;

import org.springframework.scheduling.annotation.Async;

public interface AddressSemanticizationTriggerService
{
    @Async
    void trigger();
}
