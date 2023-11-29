package com.manning.bippo.service.comps.pojo;

import java.io.Serializable;

public class CompRating implements Serializable {

    public int overall, proximity, recency, gla, year;

    public CompRating(WeightedComp weights) {
        this.proximity = CompRating.calculateProximity(weights);
        this.recency = CompRating.calculateRecency(weights);
        this.gla = CompRating.calculateGla(weights);
        this.year = CompRating.calculateYear(weights);
        this.calculateOverall();
    }

    private void calculateOverall() {
        // Take the weighted average of the individual ratings to get the overall rating
        // The weights are taken from those defined in WeightedComp
        final double total = WeightedComp.PROXIMITY_WEIGHT + WeightedComp.RECENCY_WEIGHT + WeightedComp.GLA_WEIGHT + WeightedComp.YEAR_WEIGHT + WeightedComp.SUBD_WEIGHT;
        double sum = 0d;

        // The individual rating fields are [0, 100]..
        sum += this.proximity * (WeightedComp.PROXIMITY_WEIGHT + WeightedComp.SUBD_WEIGHT);
        sum += this.recency * WeightedComp.RECENCY_WEIGHT;
        sum += this.gla * WeightedComp.GLA_WEIGHT;
        sum += this.year * WeightedComp.YEAR_WEIGHT;

        // .. so when we divide it by the total weight here, it will still be [0, 100]
        this.overall = (int) (sum / total);
    }


    private static int calculateProximity(WeightedComp weights) {
        final double distance = -weights.proximity;

        if (distance < .5 && weights.subdivision > 0) {
            // If the comp is within .5 miles and the same subdivision, it gets an automatic max rating
            return 100;
        }

        // Otherwise, calculate the rating as f(x) = -32 * distance + 98
        // This ranges from 98% at 0 miles to 0% at 3 miles
        return Math.max((int) (-32d * distance + 98d), 0);
    }

    private static int calculateRecency(WeightedComp weights) {
        final int days = (int) -weights.recency;

        if (days < 30) {
            return 100;
        }

        // Calculate the rating as f(x) = -.4 * days + 112
        // This ranges from 100% at 30 days to 0% at 280 days
        return Math.max(Math.min((int) (-.4 * days + 112d), 100), 0);
    }

    private static int calculateGla(WeightedComp weights) {
        final int glaFromTarget = -weights.gla;

        // Calculate the rating as f(x) = -.25 * glaFromTarget + 100
        // This ranges from 100% at 0 sqft to 0% at 400 sqft
        return Math.max((int) (-.25 * glaFromTarget + 100d), 0);
    }

    private static int calculateYear(WeightedComp weights) {
        final int yearFromTarget = -weights.yearBuilt;

        // Calculate the rating as f(x) = -5 * yearFromTarget + 100
        return Math.max((int) (-5d * yearFromTarget + 100d), 0);
    }
}
