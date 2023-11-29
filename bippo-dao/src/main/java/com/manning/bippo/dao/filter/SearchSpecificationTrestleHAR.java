package com.manning.bippo.dao.filter;

import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.pojo.TrestleHAR;
import com.manning.bippo.dao.pojo.TrestleHAR_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

public class SearchSpecificationTrestleHAR implements Specification<TrestleHAR>
{
    private final PropertySearchFilter propertySearchFilter;
    private List<Sort.Order> sortFields = new ArrayList<>();

    public SearchSpecificationTrestleHAR(PropertySearchFilter propertySearchFilter) {
        this.propertySearchFilter = propertySearchFilter;
    }

    public SearchSpecificationTrestleHAR(PropertySearchFilter propertySearchFilter, List<Sort.Order> acceptedSortFields) {
        this.propertySearchFilter = propertySearchFilter;
        this.sortFields = acceptedSortFields;
    }

    @Override
    public Predicate toPredicate(Root<TrestleHAR> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        if (propertySearchFilter.getPropertyType() != null) {
            predicates.add(criteriaBuilder.equal(root.get(TrestleHAR_.propertyType), propertySearchFilter.getPropertyType()));
        }

        if (propertySearchFilter.getMlsStatus() != null && propertySearchFilter.getMlsStatus().size() > 0) {
            predicates.add(root.get(TrestleHAR_.standardStatus).in(propertySearchFilter.getMlsStatus()));
        }

        if (propertySearchFilter.getPropertySubType() != null && propertySearchFilter.getPropertySubType().size() > 0) {
            predicates.add(root.get(TrestleHAR_.propertySubType).in(propertySearchFilter.getPropertySubType()));
        }

        if (propertySearchFilter.getPropertyType() != null && propertySearchFilter.getPropertyType().size() > 0) {
            predicates.add(root.get(TrestleHAR_.propertyType).in(propertySearchFilter.getPropertyType()));
        }

        if(propertySearchFilter.getNotInMLSStatus() != null && propertySearchFilter.getNotInMLSStatus().size() > 0) {
            predicates.add(criteriaBuilder.not(root.get(TrestleHAR_.standardStatus).in(propertySearchFilter.getNotInMLSStatus())));
        }

        if (propertySearchFilter.getNumberOfStories() != null) {
            predicates.add(criteriaBuilder.equal(root.get(TrestleHAR_.stories), propertySearchFilter.getNumberOfStories()));
        }
        if (propertySearchFilter.getPriceRangeFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.closePrice), propertySearchFilter.getPriceRangeFrom().doubleValue()));
        }
        if (propertySearchFilter.getPriceRangeTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.closePrice), propertySearchFilter.getPriceRangeTo().doubleValue()));
        }
        if (propertySearchFilter.getZipCodes() != null && propertySearchFilter.getZipCodes().size() > 0) {
            predicates.add(root.get(TrestleHAR_.postalCode).in(propertySearchFilter.getZipCodes()));
        }
        if (propertySearchFilter.getCity() != null && !propertySearchFilter.getCity().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get(TrestleHAR_.city), propertySearchFilter.getCity()));
        }
        if (propertySearchFilter.getCounty() != null && !propertySearchFilter.getCounty().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get(TrestleHAR_.countyOrParish), propertySearchFilter.getCounty()));
        }
        if (propertySearchFilter.getSqftFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.livingArea), propertySearchFilter.getSqftFrom().doubleValue()));
        }
        if (propertySearchFilter.getSqftTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.livingArea), propertySearchFilter.getSqftTo().doubleValue()));
        }
        if (propertySearchFilter.getLotFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.lotSizeArea), propertySearchFilter.getLotFrom().doubleValue()));
        }
        if (propertySearchFilter.getLotTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.lotSizeArea), propertySearchFilter.getLotTo().doubleValue()));
        }
        /**
         * Add search by Address here
         */

        if (propertySearchFilter.getSchoolDistrict() != null && propertySearchFilter.getSchoolDistrict().size() > 0) {
            propertySearchFilter.getSchoolDistrict().forEach(s -> predicates.add(criteriaBuilder.equal(root.get(TrestleHAR_.highSchoolDistrict), s)));
        }

        if (propertySearchFilter.getListPriceFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.listPrice), propertySearchFilter.getListPriceFrom().doubleValue()));
        }
        if (propertySearchFilter.getListPriceTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.listPrice), propertySearchFilter.getListPriceTo().doubleValue()));
        }

        /**
         * Add ARV, AS-IS, Wholesale filters here
         */

        if (propertySearchFilter.getDomFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.daysOnMarket), propertySearchFilter.getDomFrom()));
        }
        if (propertySearchFilter.getDomTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.daysOnMarket), propertySearchFilter.getDomTo()));
        }

        if (propertySearchFilter.getCdomFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.cumulativeDaysOnMarket), propertySearchFilter.getCdomFrom()));
        }
        if (propertySearchFilter.getCdomTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.cumulativeDaysOnMarket), propertySearchFilter.getCdomTo()));
        }

//        if(propertySearchFilter.getPool() != null) {
//            predicates.add(criteriaBuilder.isTrue(root.get(TrestleHAR_.poolYN)));
//        }

        if (propertySearchFilter.getBedsFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.bedroomsTotal), propertySearchFilter.getBedsFrom()));
        }
        if (propertySearchFilter.getBedsTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.bedroomsTotal), propertySearchFilter.getBedsTo()));
        }

        if (propertySearchFilter.getBathsFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.bathroomsFull), propertySearchFilter.getBathsFrom()));
        }
        if (propertySearchFilter.getBathsTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.bathroomsFull), propertySearchFilter.getBathsTo()));
        }

        if (propertySearchFilter.getHalfBathsFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.bathroomsHalf), propertySearchFilter.getHalfBathsFrom()));
        }
        if (propertySearchFilter.getHalfBathsTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.bathroomsHalf), propertySearchFilter.getHalfBathsTo()));
        }

        if (propertySearchFilter.getGarageBaysFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.garageSpaces), propertySearchFilter.getGarageBaysFrom().doubleValue()));
        }
        if (propertySearchFilter.getGarageBaysTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.garageSpaces), propertySearchFilter.getGarageBaysTo().doubleValue()));
        }

        if (propertySearchFilter.getCarportBaysFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.carportSpaces), propertySearchFilter.getCarportBaysFrom().doubleValue()));
        }
        if (propertySearchFilter.getCarportBaysTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.carportSpaces), propertySearchFilter.getCarportBaysTo().doubleValue()));
        }

        /**
         * Add HUD Filter search here.
         */

        /**
         * Add condition filter
         */

        /**
         * Add tax filter here
         */

        if (propertySearchFilter.getHoaFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(TrestleHAR_.associationFee), propertySearchFilter.getHoaFrom().doubleValue()));
        }
        if (propertySearchFilter.getHoaTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(TrestleHAR_.associationFee), propertySearchFilter.getHoaTo().doubleValue()));
        }

//        if (propertySearchFilter.getLivingAreasFrom() != null) {
//            predicates.add(criteriaBuilder.greaterThan(root.get(TrestleHAR_.numberOfLivingAreas), propertySearchFilter.getLivingAreasFrom()));
//        }
//        if (propertySearchFilter.getLivingAreasTo() != null) {
//            predicates.add(criteriaBuilder.lessThan(root.get(TrestleHAR_.numberOfLivingAreas), propertySearchFilter.getLivingAreasTo()));
//        }
//
//        if (propertySearchFilter.getDiningAreasFrom() != null) {
//            predicates.add(criteriaBuilder.greaterThan(root.get(TrestleHAR_.numberOfDiningAreas), propertySearchFilter.getDiningAreasFrom()));
//        }
//        if (propertySearchFilter.getDiningAreasTo() != null) {
//            predicates.add(criteriaBuilder.lessThan(root.get(TrestleHAR_.numberOfDiningAreas), propertySearchFilter.getDiningAreasTo()));
//        }

        if (propertySearchFilter.getAndWords() != null || propertySearchFilter.getNotWords() != null) {
            final String ftq = formatBooleanFulltext(propertySearchFilter.getAndWords(), propertySearchFilter.getNotWords());

            if (ftq != null && !ftq.isEmpty()) {
                predicates.add(criteriaBuilder.greaterThan(
                        criteriaBuilder.function("match", Double.class, root.get(TrestleHAR_.publicRemarks), criteriaBuilder.literal(ftq)), 0d));
            }
        }

        if (sortFields.size() > 0) {
            List<Order> orderByPredicates = new ArrayList<>();

            for (Sort.Order sortFeild : sortFields) {
                switch (sortFeild.getProperty()) {
                    case "mlsStatus":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.standardStatus, root, criteriaBuilder, sortFeild));
                        break;
                    case "propertySubType":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.propertySubType, root, criteriaBuilder, sortFeild));
                        break;
                    case "listPrice":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.listPrice, root, criteriaBuilder, sortFeild));
                        break;
                    case "county":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.countyOrParish, root, criteriaBuilder, sortFeild));
                        break;
                    case "city":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.city, root, criteriaBuilder, sortFeild));
                        break;
                    case "beds":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.bedroomsTotal, root, criteriaBuilder, sortFeild));
                        break;
                    case "baths":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.bathroomsTotalInteger, root, criteriaBuilder, sortFeild));
                        break;
                    case "dom":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.daysOnMarket, root, criteriaBuilder, sortFeild));
                        break;
                    case "cdom":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.cumulativeDaysOnMarket, root, criteriaBuilder, sortFeild));
                        break;
                    case "schoolDistrict":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.highSchoolDistrict, root, criteriaBuilder, sortFeild));
                        break;
                    case "yearBuilt":
                        orderByPredicates.add(getOrderByExpression(TrestleHAR_.yearBuilt, root, criteriaBuilder, sortFeild));
                        break;
                    default:
                        orderByPredicates.add(criteriaBuilder.desc(root.get(TrestleHAR_.statusChangeTimestamp)));
                }
            }

            if (orderByPredicates.size() > 0) {
                criteriaQuery.orderBy(orderByPredicates);
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }


    static Order getOrderByExpression(SingularAttribute<TrestleHAR, ?> attribute, Root<TrestleHAR> root, CriteriaBuilder criteriaBuilder, Sort.Order sortField) {
        if (sortField.getDirection() == Sort.Direction.ASC) {
            return criteriaBuilder.asc(root.get(attribute));
        } else {
            return criteriaBuilder.desc(root.get(TrestleHAR_.standardStatus));
        }
    }

    static String formatBooleanFulltext(List<String> positive, List<String> negative) {
        final StringBuilder ftquery = new StringBuilder();

        if (positive != null) {
            for (String req : positive) {
                req = req.replaceAll("[^\\w]", "").trim();

                if (req.indexOf(' ') >= 0) {
                    ftquery.append("+\"").append(req).append("\" ");
                } else {
                    ftquery.append('+').append(req).append(' ');
                }
            }
        }

        if (negative != null) {
            for (String not : negative) {
                not = not.replaceAll("[^\\w]", "").trim();

                if (not.indexOf(' ') >= 0) {
                    ftquery.append("-\"").append(not).append("\" ");
                } else {
                    ftquery.append('-').append(not).append(' ');
                }
            }
        }

        return ftquery.toString().trim();
    }
}
