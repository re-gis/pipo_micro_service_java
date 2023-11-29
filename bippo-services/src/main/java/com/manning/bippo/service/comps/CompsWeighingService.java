package com.manning.bippo.service.comps;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Doubles;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.service.comps.pojo.BracketedComps;
import com.manning.bippo.service.comps.pojo.CompRating;
import com.manning.bippo.service.comps.pojo.CompRatings;
import com.manning.bippo.service.comps.pojo.IdealComp;
import com.manning.bippo.service.comps.pojo.WeightedComp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

@Service
public class CompsWeighingService {

    private static final int INF_RANGE_MIN_PCT = -20;
    private static final int INF_RANGE_MAX_PCT = -5;
    private static final int EQ_RANGE_MIN_PCT = -5;
    private static final int EQ_RANGE_MAX_PCT = 5;
    private static final int SUP_RANGE_MIN_PCT = 5;
    private static final int SUP_RANGE_MAX_PCT = 20;
    private static final double INF_RANGE_MIN = 1d + INF_RANGE_MIN_PCT / 100d;
    private static final double INF_RANGE_MAX = 1d + INF_RANGE_MAX_PCT / 100d;
    private static final double EQ_RANGE_MIN = 1d + EQ_RANGE_MIN_PCT / 100d;
    private static final double EQ_RANGE_MAX = 1d + EQ_RANGE_MAX_PCT / 100d;
    private static final double SUP_RANGE_MIN = 1d + SUP_RANGE_MIN_PCT / 100d;
    private static final double SUP_RANGE_MAX = 1d + SUP_RANGE_MAX_PCT / 100d;
    
    private void weighComps(String subjectSubd, int subjectSqft, int subjectYear, float subjectLongitude, float subjectLatitude,
            Collection<? extends AbstractProperty> comps, WeightedComp[] sup, WeightedComp[] eq, WeightedComp[] inf) {
        final int maxPrice = comps.stream().mapToInt(p -> p.getClosePrice().intValue()).max().orElse(0);
        final int numComps = comps.size();
        final IdealComp infComp, eqComp, supComp;
        
        // Create an IdealComp for each of the inf, eq, sup comps, using ARV priceAim of max
        infComp = new IdealComp(subjectSubd, subjectSqft, subjectYear, INF_RANGE_MIN, INF_RANGE_MAX, 5, maxPrice);
        eqComp = new IdealComp(subjectSubd, subjectSqft, subjectYear, EQ_RANGE_MIN, EQ_RANGE_MAX, 5, maxPrice);
        supComp = new IdealComp(subjectSubd, subjectSqft, subjectYear, SUP_RANGE_MIN, SUP_RANGE_MAX, 5, maxPrice);
        
        final Iterator<? extends AbstractProperty> it = comps.iterator();
        
        for (int i = 0; i < numComps; i++) {
            final AbstractProperty comp = it.next();
            final double distance = this.calculateDistance(subjectLatitude, subjectLongitude, comp);
            
            inf[i] = new WeightedComp(infComp, comp, distance, i);
            eq[i] = new WeightedComp(eqComp, comp, distance, i);
            sup[i] = new WeightedComp(supComp, comp, distance, i);
        }
    }
    
    public List<CompRatings> rateComps(String subjectSubd, int subjectSqft, int subjectYear, float subjectLatitude, float subjectLongitude, Collection<? extends AbstractProperty> comps) {
        final List<CompRatings> ratings = new ArrayList<>();
        final int numComps = comps.size();
        final WeightedComp[] infWeights = new WeightedComp[numComps], eqWeights = new WeightedComp[numComps], supWeights = new WeightedComp[numComps];
        
        this.weighComps(subjectSubd, subjectSqft, subjectYear, subjectLongitude, subjectLatitude, comps, supWeights, eqWeights, infWeights);
        
        for (int i = 0; i < numComps; i++) {
            ratings.add(new CompRatings(supWeights[i], eqWeights[i], infWeights[i]));
        }
        
        return ratings;
    }
    
    public List<AbstractProperty> bracketArvComps(String subjectSubd, int subjectSqft, int subjectYear, float subjectLatitude, float subjectLongitude, String subjectApn, List<? extends AbstractProperty> comps) {
        if (subjectApn != null) {
            final String avoidApn = subjectApn.replaceAll("\\D", "");

            comps.removeIf(comp -> {
                String compApn = comp.getParcelNumber();

                if (compApn != null) {
                    compApn = compApn.replaceAll("\\D", "");

                    return avoidApn.equals(compApn);
                }

                return false;
            });
        }

        if (comps.size() <= 3) {
            final List<AbstractProperty> bracket = new ArrayList<>(comps);
            bracket.sort(AbstractProperty::compareByClosePriceDesc);
            return bracket;
        }
        
        final int numComps = comps.size();
        final WeightedComp[] infWeights = new WeightedComp[numComps], eqWeights = new WeightedComp[numComps], supWeights = new WeightedComp[numComps];
        
        this.weighComps(subjectSubd, subjectSqft, subjectYear, subjectLongitude, subjectLatitude, comps, supWeights, eqWeights, infWeights);
        
        final WeightedComp infLimits = limits(infWeights), eqLimits = limits(eqWeights), supLimits = limits(supWeights);
        final NavigableSet<WeightedComp> infSet = new TreeSet<>(), eqSet = new TreeSet<>(), supSet = new TreeSet<>();
        
        for (int i = 0; i < numComps; i++) {
            infSet.add(infWeights[i].weigh(infLimits));
            eqSet.add(eqWeights[i].weigh(eqLimits));
            supSet.add(supWeights[i].weigh(supLimits));
        }
        
        final Multiset<Integer> tempComps = HashMultiset.create();
        
        for (boolean reduced = true; reduced; tempComps.clear()) {
            final int bestInf = infSet.last().id, bestEq = eqSet.last().id, bestSup = supSet.last().id;
            reduced = false;
            Collections.addAll(tempComps, Integer.valueOf(bestInf), Integer.valueOf(bestEq), Integer.valueOf(bestSup));

            for (Multiset.Entry<Integer> entry : tempComps.entrySet()) {
                if (entry.getCount() > 1) {
                    final int id = entry.getElement().intValue();
                    // Gather the weight for each slot that this comp was present under, so we can retain the highest
                    final double[] weights = {
                            bestInf == id ? infWeights[id].weight : Double.NEGATIVE_INFINITY,
                            bestEq == id ? eqWeights[id].weight : Double.NEGATIVE_INFINITY,
                            bestSup == id ? supWeights[id].weight : Double.NEGATIVE_INFINITY
                    };

                    switch (Doubles.indexOf(weights, Doubles.max(weights))) {
                        case 0: // Its inferior is the best match
                            eqSet.remove(eqWeights[id]);
                            supSet.remove(supWeights[id]);
                            break;
                        case 1: // Its equal is the best match
                            infSet.remove(infWeights[id]);
                            supSet.remove(supWeights[id]);
                            break;
                        case 2: // Its superior is the best match
                            infSet.remove(infWeights[id]);
                            eqSet.remove(eqWeights[id]);
                            break;
                        default:
                            throw new AssertionError();
                    }

                    reduced = true;
                }
            }
        }
        
        return Lists.newArrayList(comps.get(supSet.last().id), comps.get(eqSet.last().id), comps.get(infSet.last().id));
    }
    
    public static WeightedComp limits(WeightedComp[] of) {
        double proximity = 0d;
        long recency = 0L;
        int gla = 0, yearBuilt = 0, price = 0;
        
        for (WeightedComp comp : of) {
            if (Math.abs(comp.proximity) > proximity) {
                proximity = Math.abs(comp.proximity);
            }
            
            if (Math.abs(comp.recency) > recency) {
                recency = Math.abs(comp.recency);
            }
            
            if (Math.abs(comp.gla) > gla) {
                gla = Math.abs(comp.gla);
            }
            
            if (Math.abs(comp.yearBuilt) > yearBuilt) {
                yearBuilt = Math.abs(comp.yearBuilt);
            }
            
            if (Math.abs(comp.price) > price) {
                price = Math.abs(comp.price);
            }
        }
        
        return new WeightedComp(proximity, recency, gla, yearBuilt, price, 2);
    }

    public double calculateDistance(Float subjectLatitude, Float subjectLongitude, AbstractProperty compProperty)
    {
        if (subjectLatitude != null && subjectLongitude != null
                && compProperty.getLatitude() != null && compProperty.getLongitude() != null)
        {
            return Precision.round(LatLngTool.distance(new LatLng(subjectLatitude, subjectLongitude),
                    new LatLng(compProperty.getLatitude().floatValue(), compProperty.getLongitude().floatValue()), LengthUnit.MILE), 2);
        } else
        {
            return 0;
        }
    }
}
