package com.manning.bippo.service.rets.impl;

import com.manning.bippo.commons.dto.CompsFilter;
import java.util.List;

public class CompsFilterBuilder {

    private final int radiusMin, radiusMax, glaMin, glaMax, daysMin, daysMax;
    private final double radiusInc;
    private final int glaInc, daysInc;

    public CompsFilterBuilder(int radiusMin, int radiusMax, double radiusInc, int glaMin, int glaMax, int glaInc, int daysMin, int daysMax, int daysInc) {
        this.radiusMin = radiusMin;
        this.radiusMax = radiusMax;
        this.glaMin = glaMin;
        this.glaMax = glaMax;
        this.daysMin = daysMin;
        this.daysMax = daysMax;
        this.radiusInc = radiusInc;
        this.glaInc = glaInc;
        this.daysInc = daysInc;
    }

    public void build(List<CompsFilter> into) {
        for (int days = this.daysMin; days <= this.daysMax; days += this.daysInc) {
            for (double radius = this.radiusMin; radius <= this.radiusMax; radius += this.radiusInc) {
                for (int gla = this.glaMin; gla <= this.glaMax; gla += this.glaInc) {
                    into.add(build(radius, gla, days));
                }
            }
        }
    }
    
    private static CompsFilter build(double proximityInMiles, int glaPercentWithIn, int statusChangeInDays) {
        CompsFilter compsFilter = new CompsFilter();
        compsFilter.statusChangeInDays = statusChangeInDays;
        compsFilter.sqftTotalWithIn = glaPercentWithIn;
        compsFilter.proximityInMiles = proximityInMiles;
        return compsFilter;
    }
}
