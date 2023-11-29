package com.manning.bippo.service.comps.pojo;

import com.manning.bippo.dao.itf.AbstractProperty;
import java.util.concurrent.TimeUnit;
import lombok.ToString;

@ToString
public class WeightedComp implements Comparable<WeightedComp> {

    public static final double PROXIMITY_WEIGHT = 2.25d;
    public static final double RECENCY_WEIGHT = .5d;
    public static final double GLA_WEIGHT = 2d;
    public static final double YEAR_WEIGHT = 1d;
    public static final double PRICE_WEIGHT = 2.25d;
    public static final double SUBD_WEIGHT = 1d;
    
    public final double proximity; // fuzzy weights
    public final long recency;
    public final int id, gla, yearBuilt, price, subdivision;
    public double weight;
    
    public WeightedComp(IdealComp ideal, AbstractProperty comp, double distance, int id) {
        this.id = id;
        this.proximity = -distance; 
        this.recency = TimeUnit.MILLISECONDS.toDays(comp.getStatusChangeTimestamp().getTime()) - ideal.statusChange;
        this.gla = WeightedComp.constrain(comp.getSqFtTotal().intValue(), ideal.glaMin, ideal.glaMax);
        this.yearBuilt = WeightedComp.constrain(comp.getYearBuilt(), ideal.yearBuiltMin, ideal.yearBuiltMax);
        this.price = -Math.abs(comp.getClosePrice().intValue() / 1000 - ideal.priceAim);

        final String subd = comp.getSubdivisionName(), match = ideal.subdivision;

        if (subd == null || match == null) {
            this.subdivision = 0;
        } else if (subd.equalsIgnoreCase(match)) {
            this.subdivision = 2;
        } else if (subd.length() >= 8 && match.length() >= 8 && subd.substring(0, 8).equalsIgnoreCase(match.substring(0, 8))) {
            // Match near misses, subdivisions are frequently numbered
            this.subdivision = 1;
        } else {
            this.subdivision = 0;
        }

        this.weight = Double.NaN;
    }
    
    public WeightedComp(double proximity, long recency, int gla, int yearBuilt, int price, int subdivision) {
        this.id = -1;
        this.proximity = proximity;
        this.recency = recency;
        this.gla = gla;
        this.yearBuilt = yearBuilt;
        this.price = price;
        this.subdivision = subdivision;
        this.weight = Double.NaN;
    }
    
    @Override
    public int compareTo(WeightedComp o) {
        if (!Double.isFinite(this.weight) || !Double.isFinite(o.weight)) {
            throw new IllegalStateException();
        }
        
        final int cmp = Double.compare(this.weight, o.weight);
        // If our weights collide, only actually return 0 if we were given the same instance as ourself
        // Otherwise, maintain a stable sort
        return cmp != 0 ? cmp : o == this ? 0 : -1;
    }
    
    public WeightedComp weigh(WeightedComp against) {
        double weight = 0d;
        
        if (against.proximity != 0d) {
            weight += this.proximity / against.proximity * WeightedComp.PROXIMITY_WEIGHT;
        }
        
        if (against.recency != 0L) {
            weight += this.recency / (double) against.recency * WeightedComp.RECENCY_WEIGHT;
        }
        
        if (against.gla != 0) {
            weight += this.gla / (double) against.gla * WeightedComp.GLA_WEIGHT;
        }
        
        if (against.yearBuilt != 0) {
            weight += this.yearBuilt / (double) against.yearBuilt * WeightedComp.YEAR_WEIGHT;
        }
        
        if (against.price != 0) {
            weight += this.price / (double) against.price * WeightedComp.PRICE_WEIGHT;
        }
        
        weight += this.subdivision / 2d * WeightedComp.SUBD_WEIGHT;
        
        this.weight = weight;
        return this;
    }
    
    
    public static int constrain(int given, int min, int max) {
        if (Math.floorDiv(given - min, max - min + 1) == 0) {
            return 0;
        }
        
        return -Math.abs(given - min);
    }
}
