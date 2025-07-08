package com.portal.bid.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.portal.bid.entity.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
    
    // Custom queries for analytics
    List<Lead> findByPartFyId(Long fyId);
    List<Lead> findByIndustrySegmentId(Long industrySegmentId);
    List<Lead> findByPartFyIdAndIndustrySegmentId(Long fyId, Long industrySegmentId);
    
    // ✅ New method to fetch all leads using specification & sorting
    List<Lead> findAll(Specification<Lead> spec, Sort sort);
}
