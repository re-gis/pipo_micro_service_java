package com.manning.bippo.dao.filter;

import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.pojo.NtreisProperty;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

public class PropertySearchSpecification implements Specification<NtreisProperty>
{
    private final PropertySearchFilter propertySearchFilter;
    private List<Sort.Order> sortFields = new ArrayList<>();

    public PropertySearchSpecification(PropertySearchFilter propertySearchFilter) {
        this.propertySearchFilter = propertySearchFilter;
    }

    public PropertySearchSpecification(PropertySearchFilter propertySearchFilter, List<Sort.Order> acceptedSortFields) {
        this.propertySearchFilter = propertySearchFilter;
        this.sortFields = acceptedSortFields;
    }

    @Override
    public Predicate toPredicate(Root<NtreisProperty> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        if (propertySearchFilter.getPropertyType() != null) {
            predicates.add(criteriaBuilder.equal(root.get(NtreisProperty_.propertyType), propertySearchFilter.getPropertyType()));
        }

        if (propertySearchFilter.getMlsStatus() != null && propertySearchFilter.getMlsStatus().size() > 0) {
            predicates.add(root.get(NtreisProperty_.status).in(propertySearchFilter.getMlsStatus()));
        }

        if (propertySearchFilter.getPropertySubType() != null && propertySearchFilter.getPropertySubType().size() > 0) {
            predicates.add(root.get(NtreisProperty_.propertySubType).in(propertySearchFilter.getPropertySubType()));
        }

        if (propertySearchFilter.getPropertyType() != null && propertySearchFilter.getPropertyType().size() > 0) {
            predicates.add(root.get(String.valueOf(NtreisProperty_.propertyType)).in(propertySearchFilter.getPropertyType()));
        }

        if(propertySearchFilter.getNotInMLSStatus() != null && propertySearchFilter.getNotInMLSStatus().size() > 0) {
            predicates.add(criteriaBuilder.not(root.get(NtreisProperty_.status).in(propertySearchFilter.getNotInMLSStatus())));
        }

        if (propertySearchFilter.getNumberOfStories() != null) {
            predicates.add(criteriaBuilder.equal(root.get(String.valueOf(NtreisProperty_.numberOfStories)), propertySearchFilter.getNumberOfStories()));
        }
        if (propertySearchFilter.getPriceRangeFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.closePrice)), propertySearchFilter.getPriceRangeFrom()));
        }
        if (propertySearchFilter.getPriceRangeTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.closePrice)), propertySearchFilter.getPriceRangeTo()));
        }
        if (propertySearchFilter.getZipCodes() != null && propertySearchFilter.getZipCodes().size() > 0) {
            // predicates.add(NtreisProperty_.postalCode.as(Boolean.class)).in(propertySearchFilter.getZipCodes());
            Expression<?> postalCodeExpression = root.get(NtreisProperty_.postalCode);
            List<Integer> zipCodes = propertySearchFilter.getZipCodes();

            // Create a list to hold the predicates for each zip code
            List<Predicate> zipCodePredicates = new ArrayList<>();

            for (Integer zipCode : zipCodes) {
                Predicate zipCodePredicate = criteriaBuilder.equal(postalCodeExpression, zipCode);
                zipCodePredicates.add(zipCodePredicate);
            }

            // Combine the individual predicates with an "OR" condition
            Predicate finalZipCodePredicate = criteriaBuilder.or(zipCodePredicates.toArray(new Predicate[0]));

            // Add the combined predicate to the list of overall predicates
            predicates.add(finalZipCodePredicate);
        }
        if (propertySearchFilter.getCity() != null && !propertySearchFilter.getCity().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get(String.valueOf(NtreisProperty_.city)), propertySearchFilter.getCity()));
        }
        if (propertySearchFilter.getCounty() != null && !propertySearchFilter.getCounty().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get(String.valueOf(NtreisProperty_.countyOrParish)), propertySearchFilter.getCounty()));
        }
        if (propertySearchFilter.getSqftFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.sqFtTotal)), propertySearchFilter.getSqftFrom()));
        }
        if (propertySearchFilter.getSqftTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.sqFtTotal)), propertySearchFilter.getSqftTo()));
        }
        if (propertySearchFilter.getLotFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(NtreisProperty_.lotSizeAreaSQFT), propertySearchFilter.getLotFrom()));
        }
        if (propertySearchFilter.getLotTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(NtreisProperty_.lotSizeAreaSQFT), propertySearchFilter.getLotTo()));
        }
        /**
         * Add search by Address here
         */

        if (propertySearchFilter.getSchoolDistrict() != null && propertySearchFilter.getSchoolDistrict().size() > 0) {
            propertySearchFilter.getSchoolDistrict().forEach(s -> predicates.add(criteriaBuilder.equal(root.get(NtreisProperty_.schoolDistrict), s)));
        }

        if (propertySearchFilter.getListPriceFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.listPrice)), propertySearchFilter.getListPriceFrom()));
        }
        if (propertySearchFilter.getListPriceTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.listPrice)), propertySearchFilter.getListPriceTo()));
        }

        /**
         * Add ARV, AS-IS, Wholesale filters here
         */

        if (propertySearchFilter.getDomFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.dOM)), propertySearchFilter.getDomFrom()));
        }
        if (propertySearchFilter.getDomTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.dOM)), propertySearchFilter.getDomTo()));
        }

        if (propertySearchFilter.getCdomFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.cDOM)), propertySearchFilter.getCdomFrom()));
        }
        if (propertySearchFilter.getCdomTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.cDOM)), propertySearchFilter.getCdomTo()));
        }

//        if(propertySearchFilter.getPool() != null) {
//            predicates.add(criteriaBuilder.isTrue(root.get(NtreisProperty_.poolYN)));
//        }

        if (propertySearchFilter.getBedsFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.bedsTotal)), propertySearchFilter.getBedsFrom()));
        }
        if (propertySearchFilter.getBedsTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.bedsTotal)), propertySearchFilter.getBedsTo()));
        }

        if (propertySearchFilter.getBathsFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.bathsFull)), propertySearchFilter.getBathsFrom()));
        }
        if (propertySearchFilter.getBathsTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.bathsFull)), propertySearchFilter.getBathsTo()));
        }

        if (propertySearchFilter.getHalfBathsFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.bathsHalf)), propertySearchFilter.getHalfBathsFrom()));
        }
        if (propertySearchFilter.getHalfBathsTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.bathsHalf)), propertySearchFilter.getHalfBathsTo()));
        }

        if (propertySearchFilter.getGarageBaysFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.parkingSpacesGarage)), propertySearchFilter.getGarageBaysFrom()));
        }
        if (propertySearchFilter.getGarageBaysTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.parkingSpacesGarage)), propertySearchFilter.getGarageBaysTo()));
        }

        if (propertySearchFilter.getCarportBaysFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.parkingSpacesCarport)), propertySearchFilter.getCarportBaysFrom()));
        }
        if (propertySearchFilter.getCarportBaysTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.parkingSpacesCarport)), propertySearchFilter.getCarportBaysTo()));
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
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.associationFee)), propertySearchFilter.getHoaFrom()));
        }
        if (propertySearchFilter.getHoaTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.associationFee)), propertySearchFilter.getHoaTo()));
        }

        if (propertySearchFilter.getLivingAreasFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.numberOfLivingAreas)), propertySearchFilter.getLivingAreasFrom()));
        }
        if (propertySearchFilter.getLivingAreasTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.numberOfLivingAreas)), propertySearchFilter.getLivingAreasTo()));
        }

        if (propertySearchFilter.getDiningAreasFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.numberOfDiningAreas)), propertySearchFilter.getDiningAreasFrom()));
        }
        if (propertySearchFilter.getDiningAreasTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(String.valueOf(NtreisProperty_.numberOfDiningAreas)), propertySearchFilter.getDiningAreasTo()));
        }

        if (propertySearchFilter.getAndWords() != null || propertySearchFilter.getNotWords() != null) {
            final String ftq = formatBooleanFulltext(propertySearchFilter.getAndWords(), propertySearchFilter.getNotWords());

            if (ftq != null && !ftq.isEmpty()) {
                predicates.add(criteriaBuilder.greaterThan(
                        criteriaBuilder.function("match", Double.class, root.get(NtreisProperty_.publicRemarks), criteriaBuilder.literal(ftq)), 0d));
            }
        }

        if (sortFields.size() > 0) {
            List<Order> orderByPredicates = new ArrayList<>();

            for (Sort.Order sortFeild : sortFields) {
                switch (sortFeild.getProperty()) {
                    case "mlsStatus":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.status, root, criteriaBuilder, sortFeild));
                        break;
                    case "propertySubType":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.propertySubType, root, criteriaBuilder, sortFeild));
                        break;
                    case "listPrice":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.listPrice, root, criteriaBuilder, sortFeild));
                        break;
                    case "county":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.countyOrParish, root, criteriaBuilder, sortFeild));
                        break;
                    case "city":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.city, root, criteriaBuilder, sortFeild));
                        break;
                    case "beds":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.bedsTotal, root, criteriaBuilder, sortFeild));
                        break;
                    case "baths":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.bathsTotal, root, criteriaBuilder, sortFeild));
                        break;
                    case "dom":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.dOM, root, criteriaBuilder, sortFeild));
                        break;
                    case "cdom":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.cDOM, root, criteriaBuilder, sortFeild));
                        break;
                    case "schoolDistrict":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.schoolDistrict, root, criteriaBuilder, sortFeild));
                        break;
                    case "yearBuilt":
                        orderByPredicates.add(getOrderByExpression(NtreisProperty_.yearBuilt, root, criteriaBuilder, sortFeild));
                        break;
                    default:
                        orderByPredicates.add(criteriaBuilder.desc(root.get(NtreisProperty_.statusChangeTimestamp)));
                }
            }

            if (orderByPredicates.size() > 0) {
                criteriaQuery.orderBy(orderByPredicates);
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }


    static Order getOrderByExpression(SingularAttribute<NtreisProperty, ?> attribute, Root<NtreisProperty> root, CriteriaBuilder criteriaBuilder, Sort.Order sortField) {
        if (sortField.getDirection() == Sort.Direction.ASC) {
            return criteriaBuilder.asc(root.get(attribute));
        } else {
            return criteriaBuilder.desc(root.get(NtreisProperty_.status));
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
