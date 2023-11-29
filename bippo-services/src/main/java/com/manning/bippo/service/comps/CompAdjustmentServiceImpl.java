package com.manning.bippo.service.comps;

import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.comps.pojo.CompAdjustments;
import com.manning.bippo.service.property.BasicPropertyBuilderService;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CompAdjustmentServiceImpl implements CompAdjustmentService
{
    /*
     * This will need to be abstracted into a database-based design soon
     * Multiple locations (e.g. different states) will eventually need to be supported
    ---
     * These adjustments will eventually (in the far future) be inferred through paring analysis, where
     *   the effect of each amenity (respective to the market area) will be isolated programatically.
     * Approximations of these values are currently specified manually as these systems are not yet feasible.
     */
    private static final int[] adjustmentGrades = { 60_000, 120_000, 200_000, 250_000, 500_000, 1_000_000, 5_000_000, Integer.MAX_VALUE };
    private static final int[] adjustBathsFull =  {   1500,    3000,    5000,    5000,    5000,      6000,     15000,             25000 };
    private static final int[] adjustBathsHalf =  {    500,    1500,    2500,    2500,    2500,      3500,      7500,             12500 };
    private static final int[] adjustBedrooms =   {   1000,    2000,    2000,    2000,    4000,      4000,         0,                 0 };
    private static final int[] adjustCarports =   {    500,     500,     500,    2000,    2000,      2000,         0,                 0 };
    private static final int[] adjustGarages =    {   2500,    5000,   10000,   10000,   10000,     15000,     25000,             50000 };
    private static final int[] adjustPools =      {  10000,   10000,   20000,   20000,   25000,     25000,     25000,             25000 };

    @Override
    public CompAdjustments adjustComp(NtreisProperty subject, AbstractProperty comp, Collection<? extends AbstractProperty> allComps)
    {
        return adjustComp(subject.getSqFtTotal(), getSqftPackagePrice(allComps),
                subject.getBathsFull(), subject.getBathsHalf(),
                subject.getBedsTotal(),
                subject.getParkingSpacesCarport(),
                subject.getParkingSpacesGarage(),
                BasicPropertyBuilderService.mapPoolInd(subject.getPoolYN()), comp);
    }

    @Override
    public CompAdjustments adjustComp(BasicPropertyDetails subject, AbstractProperty comp, Collection<? extends AbstractProperty> allComps)
    {
        return adjustComp(subject.getLivingSize(), getSqftPackagePrice(allComps),
                subject.getBathsFull(), subject.getBathsHalf(),
                subject.getBedsTotal(),
                subject.getParkingSpacesCarport(),
                subject.getParkingSpacesGarage(),
                subject.getPool(), comp);
    }

    public CompAdjustments adjustComp(Number subjectSqft, double sqftPackagePrice,
            Integer subjectBathsFull, Integer subjectBathsHalf,
            Integer subjectBedsTotal, Integer subjectParkingSpacesCarport,
            Integer subejctParkingSpacesGarage, Boolean subjectPool, AbstractProperty comp)
    {
        final int grade = CompAdjustmentServiceImpl.getAdjustmentGrade(comp, CompAdjustmentServiceImpl.adjustmentGrades);
        final CompAdjustments adjustments = new CompAdjustments(grade);

        if (subjectBathsFull != null && subjectBathsFull.intValue() > 0) {
            adjustments.adjustBathsFull(adjustByDifference(adjustBathsFull[grade], subjectBathsFull, comp.getBathsFull()));
        } else {
            adjustments.adjustBathsFull(0);
        }

        if (subjectBathsHalf != null && subjectBathsHalf.intValue() > 0) {
            adjustments.adjustBathsHalf(adjustByDifference(adjustBathsHalf[grade], subjectBathsHalf, comp.getBathsHalf()));
        } else {
            adjustments.adjustBathsHalf(0);
        }

        if (subjectBedsTotal != null && subjectBedsTotal.intValue() > 0) {
            adjustments.adjustBedrooms(adjustByDifference(adjustBedrooms[grade], subjectBedsTotal, comp.getBedsTotal()));
        } else {
            adjustments.adjustBedrooms(0);
        }

        adjustments.adjustCarports(adjustByDifference(adjustCarports[grade], subjectParkingSpacesCarport, comp.getParkingSpacesCarport()));
        adjustments.adjustGarages(adjustByDifference(adjustGarages[grade], subejctParkingSpacesGarage, comp.getParkingSpacesGarage()));

        final Boolean compPool = BasicPropertyBuilderService.mapPoolInd(comp.getPoolYN());

        if (compPool != null && subjectPool != null && subjectPool.booleanValue() != compPool.booleanValue()) {
            adjustments.adjustPool(adjustByDifference(adjustPools[grade], subjectPool ? 1 : 0, compPool ? 1 : 0));
        }

        if (subjectSqft != null && comp.getSqFtTotal() != null) {
            adjustments.adjustFootage(adjustByDifference(
                    (int) Math.ceil(sqftPackagePrice / 5d), subjectSqft.intValue(), comp.getSqFtTotal().intValue()));
        }

        adjustments.apply(comp);
        return adjustments;
    }


    public static int getAdjustmentGrade(AbstractProperty prop, int[] grades) {
        final int price = prop.getListPrice().intValue();

        for (int i = 0, low = 0; i < grades.length; low = grades[i++]) {
            if (price >= low && price < grades[i]) {
                return i;
            }
        }

        return 0;
    }

    public static int adjustByDifference(int perUnit, Integer subject, Integer comp) {
        // Subtract the comp's value from the subject's for the correct sign of the delta
        final int delta = (subject == null ? 0 : subject.intValue()) - (comp == null ? 0 : comp.intValue());
        return perUnit * delta;
    }

    public static double getSqftPackagePrice(Collection<? extends AbstractProperty> comps) {
        double total = 0d;
        int count = 0;

        for (AbstractProperty comp : comps) {
            if (comp.getSqFtTotal() != null) {
                count++;
                total += comp.getClosePrice().doubleValue() / comp.getSqFtTotal().doubleValue();
            }
        }

        return total / count;
    }
}
