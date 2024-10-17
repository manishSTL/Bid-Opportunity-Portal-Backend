package com.portal.bid.repository;

import com.portal.bid.entity.AgpData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgpDataRepository extends JpaRepository<AgpData, Long> {
    List<AgpData> findByUserIdInAndDepartmentIdAndFinancialYearAndQuarterAndBusinessSegmentId(
            List<Long> userIds, Long departmentId, String financialYear, String quarter, Long businessSegmentId);

    List<AgpData> findByDepartmentIdAndFinancialYearAndQuarterAndBusinessSegmentId(
            Long departmentId, String financialYear, String quarter, Long businessSegmentId);

    Optional<AgpData> findByUserIdAndDepartmentIdAndFinancialYearAndQuarterAndBusinessSegmentId(
            Long userId, Long departmentId, String financialYear, String quarter, Long businessSegmentId);
}