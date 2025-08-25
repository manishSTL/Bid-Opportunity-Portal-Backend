package com.portal.bid.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.portal.bid.entity.Lead;

public class LeadSpecification {

    public static Specification<Lead> withFyId(Long fyId) {
        return (root, query, criteriaBuilder) -> {
            if (fyId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("partFy").get("id"), fyId);
        };
    }

    public static Specification<Lead> withObFyId(Long obFyId) {
        return (root, query, criteriaBuilder) -> {
            if (obFyId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("obFy").get("id"), obFyId);
        };
    }
    
    public static Specification<Lead> withIndustrySegmentId(Long industrySegmentId) {
        return (root, query, criteriaBuilder) -> {
            if (industrySegmentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("industrySegment").get("id"), industrySegmentId);
        };
    }
    
    public static Specification<Lead> withPublicPrivate(String publicPrivate) {
        return (root, query, criteriaBuilder) -> {
            if (publicPrivate == null || publicPrivate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("publicPrivate"), publicPrivate);
        };
    }
    
    public static Specification<Lead> withDealStatusId(Long dealStatusId) {
        return (root, query, criteriaBuilder) -> {
            if (dealStatusId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("dealStatus").get("id"), dealStatusId);
        };
    }
    
    public static Specification<Lead> withPriorityId(Long priorityId) {
        return (root, query, criteriaBuilder) -> {
            if (priorityId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("priority").get("id"), priorityId);
        };
    }
    
    public static Specification<Lead> withPrimaryOfferingSegment(String primaryOfferingSegment) {
        return (root, query, criteriaBuilder) -> {
            if (primaryOfferingSegment == null || primaryOfferingSegment.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("primaryOfferingSegment"), primaryOfferingSegment);
        };
    }
    
    public static Specification<Lead> withAmountGreaterThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) -> {
            if (amount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amount);
        };
    }
    
    public static Specification<Lead> withAmountLessThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) -> {
            if (amount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("amount"), amount);
        };
    }
    
    public static Specification<Lead> withBidSubmissionDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("bidSubmissionDate"), endDate);
            }
            if (endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("bidSubmissionDate"), startDate);
            }
            return criteriaBuilder.between(root.get("bidSubmissionDate"), startDate, endDate);
        };
    }
    
    public static Specification<Lead> withProbabilityGreaterThan(Integer probability) {
        return (root, query, criteriaBuilder) -> {
            if (probability == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("probability"), probability);
        };
    }
    
    // Method to combine multiple specifications
    public static Specification<Lead> buildSpecification(
            Long fyId, 
            Long obFyId,
            Long industrySegmentId, 
            String publicPrivate, 
            Long dealStatusId,
            Long priorityId,
            String primaryOfferingSegment,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate startDate,
            LocalDate endDate,
            Integer minProbability) {
        
        return Specification
                .where(withFyId(fyId))
                .and(withObFyId(obFyId))
                .and(withIndustrySegmentId(industrySegmentId))
                .and(withPublicPrivate(publicPrivate))
                .and(withDealStatusId(dealStatusId))
                .and(withPriorityId(priorityId))
                .and(withPrimaryOfferingSegment(primaryOfferingSegment))
                .and(withAmountGreaterThan(minAmount))
                .and(withAmountLessThan(maxAmount))
                .and(withBidSubmissionDateBetween(startDate, endDate))
                .and(withProbabilityGreaterThan(minProbability));
    }
}