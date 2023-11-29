package com.manning.bippo.service.comps.pojo;

import java.util.concurrent.TimeUnit;
import lombok.ToString;

@ToString
public class IdealComp {

    public final String subdivision;
    public final long statusChange;
    public final int glaMin, glaMax, yearBuiltMin, yearBuiltMax, priceAim;
    
    public IdealComp(String subdivision, int sqFtTotal, int yearBuilt, double glaMinMult, double glaMaxMult, int yearBuiltRange, int priceAim) {
        this.subdivision = subdivision;
        this.statusChange = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
        this.glaMin = (int) (glaMinMult * sqFtTotal);
        this.glaMax = (int) (glaMaxMult * sqFtTotal);
        this.yearBuiltMin = yearBuilt - yearBuiltRange;
        this.yearBuiltMax = yearBuilt+ yearBuiltRange;
        this.priceAim = priceAim / 1000;
    }
}
