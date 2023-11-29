package com.manning.bippo.service.comps.pojo;

public class Adjustment {

    public Type description;
    public int adjustmentValue;

    public Adjustment(Type type, int value) {
        this.description = type;
        this.adjustmentValue = value;
    }

    public static enum Type {

        FULL_BATHS, HALF_BATHS, BEDROOMS, CARPORTS, GARAGES, POOL, FOOTAGE;

        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
