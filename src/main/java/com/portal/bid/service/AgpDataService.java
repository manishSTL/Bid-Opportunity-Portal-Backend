package com.portal.bid.service;

import com.portal.bid.entity.AgpData;
import com.portal.bid.repository.AgpDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgpDataService {

    @Autowired
    private AgpDataRepository agpDataRepository;

    @Transactional
    public AgpData addAgpData(AgpData agpData) {
        agpData.setCreatedAt(LocalDateTime.now());
        agpData.setUpdatedAt(LocalDateTime.now());
        return agpDataRepository.save(agpData);
    }

    @Transactional
    public Optional<AgpData> editAgpData(Long id, AgpData updatedAgpData) {
        return agpDataRepository.findById(id)
                .map(existingAgpData -> {
                    existingAgpData.setUser(updatedAgpData.getUser());
                    existingAgpData.setDepartment(updatedAgpData.getDepartment());
                    existingAgpData.setBusinessSegment(updatedAgpData.getBusinessSegment());
                    existingAgpData.setFinancialYear(updatedAgpData.getFinancialYear());
                    existingAgpData.setQuarter(updatedAgpData.getQuarter());
                    existingAgpData.setAgpValue(updatedAgpData.getAgpValue());
                    existingAgpData.setUpdatedAt(LocalDateTime.now());
                    return agpDataRepository.save(existingAgpData);
                });
    }

    public List<AgpData> getAllAgpData() {
        return agpDataRepository.findAll();
    }

    public List<AgpData> getAgpDataByUserIds(List<Long> userIds, Long departmentId, String financialYear, String quarter, Long businessSegmentId) {
        return agpDataRepository.findByUserIdInAndDepartmentIdAndFinancialYearAndQuarterAndBusinessSegmentId(
                userIds, departmentId, financialYear, quarter, businessSegmentId);
    }

    public List<AgpData> getAgpDataByFilters(Long departmentId, String financialYear, String quarter, Long businessSegmentId) {
        return agpDataRepository.findByDepartmentIdAndFinancialYearAndQuarterAndBusinessSegmentId(
                departmentId, financialYear, quarter, businessSegmentId);
    }

    public Optional<AgpData> getAgpDataByUserIdAndFilters(Long userId, Long departmentId, String financialYear, String quarter, Long businessSegmentId) {
        return agpDataRepository.findByUserIdAndDepartmentIdAndFinancialYearAndQuarterAndBusinessSegmentId(
                userId, departmentId, financialYear, quarter, businessSegmentId);
    }

    @Transactional
    public boolean deleteAgpData(Long id) {
        if (agpDataRepository.existsById(id)) {
            agpDataRepository.deleteById(id);
            return true;
        }
        return false;
    }
}