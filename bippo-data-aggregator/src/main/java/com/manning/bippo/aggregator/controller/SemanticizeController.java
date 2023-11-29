package com.manning.bippo.aggregator.controller;

import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.address_semanticize.pojo.SemanticsIds;
import com.manning.bippo.service.profiling.ProfilingMetricsService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/semanticize-trigger")
public class SemanticizeController
{
    @Autowired
    AddressSemanticizationService addressSemanticizationService;
    @Autowired
    ProfilingMetricsService profilingMetricsService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> semanticize(@RequestParam(name = "address") String address,
            @RequestParam(required = false, name = "taxId") Long taxId,
            @RequestParam(required = false, name = "ntreisId") Long ntreisId,
            @RequestParam(required = false, name = "auctionId") Long auctionId)
    {
        final SemanticsIds ids = SemanticsIds.builder()
                .taxId(taxId)
                .ntreisId(ntreisId)
                .auctionId(auctionId).build();
        
        try {
            this.profilingMetricsService.incrementCounter("ss_SemanticizeController_semanticize");
            this.addressSemanticizationService.semanticize(address, ids);
            return new ResponseEntity<>("Address semanticization successful.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Address semanticization failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
