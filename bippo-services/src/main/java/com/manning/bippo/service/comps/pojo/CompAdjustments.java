package com.manning.bippo.service.comps.pojo;

import com.manning.bippo.dao.itf.AbstractProperty;
import java.util.ArrayList;
import java.util.List;

public class CompAdjustments {

    public final int grade;
    /*
     * TODO: Consider using the String in Adjustment to provide a more
     * informative user output message, outlining the adjustment's purpose
     * and/or details (e.g. "2 baths vs 3 baths")
     */
    public List<Adjustment> allAdjustments;
    public Adjustment bathsFull, bathsHalf, bedrooms, carports, garages, pools, footage;
    public int netAdjustment, postAdjustment;

    public CompAdjustments(int grade) {
        this.allAdjustments = new ArrayList<>();
        this.grade = grade;
        this.netAdjustment = 0;
        this.postAdjustment = 0;
    }

    public void apply(AbstractProperty property) {
        this.postAdjustment = (property.getClosePrice() == null ? 0 : property.getClosePrice().intValue()) + this.netAdjustment;
    }

    private void apply(Adjustment a) {
        this.allAdjustments.add(a);
        this.netAdjustment += a.adjustmentValue;
    }

    private void revert(Adjustment a) {
        if (a == null) {
            return;
        }

        this.netAdjustment -= a.adjustmentValue;
        this.allAdjustments.remove(a);
    }

    public void adjustBathsFull(int adjustment) {
        this.revert(this.bathsFull);
        this.apply(this.bathsFull = new Adjustment(Adjustment.Type.FULL_BATHS, adjustment));
    }

    public void adjustBathsHalf(int adjustment) {
        this.revert(this.bathsHalf);
        this.apply(this.bathsHalf = new Adjustment(Adjustment.Type.HALF_BATHS, adjustment));
    }

    public void adjustBedrooms(int adjustment) {
        this.revert(this.bedrooms);
        this.apply(this.bedrooms = new Adjustment(Adjustment.Type.BEDROOMS, adjustment));
    }

    public void adjustCarports(int adjustment) {
        this.revert(this.carports);
        this.apply(this.carports = new Adjustment(Adjustment.Type.CARPORTS, adjustment));
    }

    public void adjustGarages(int adjustment) {
        this.revert(this.garages);
        this.apply(this.garages = new Adjustment(Adjustment.Type.GARAGES, adjustment));
    }

    public void adjustPool(int adjustment) {
        this.revert(this.pools);
        this.apply(this.pools = new Adjustment(Adjustment.Type.POOL, adjustment));
    }

    public void adjustFootage(int adjustment) {
        this.revert(this.footage);
        this.apply(this.footage = new Adjustment(Adjustment.Type.FOOTAGE, adjustment));
    }
}
