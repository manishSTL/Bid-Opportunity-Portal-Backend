package com.portal.bid.service.implementation;

import com.portal.bid.entity.DealStatus;
import com.portal.bid.entity.GoNoGoStatus;
import com.portal.bid.repository.DealStatusRepository;
import com.portal.bid.service.DealStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealStatusImp implements DealStatusService {
    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Override
    public DealStatus createDealStatus(DealStatus dealStatus) {
        return dealStatusRepository.save(dealStatus);
    }

    @Override
    public DealStatus getDealStatusById(Long id) {
        return dealStatusRepository.findById(id).orElse(null);
    }

    @Override
    public List<DealStatus> getAllDealStatus() {
        return dealStatusRepository.findAll();
    }

    public List<DealStatus> getAllDealStatus1(Long formId) {
        return dealStatusRepository.findAll().stream()
                .filter(plan -> plan.getForm_id().equals(formId))
                .collect(Collectors.toList());
    }
    @Override
    public DealStatus updateDealStatus(Long id, DealStatus dealStatus) {
        Optional<DealStatus> existingDealStatus = dealStatusRepository.findById(id);
        if (existingDealStatus.isPresent()) {
            DealStatus updatedDealStatus = existingDealStatus.get();
            updatedDealStatus.setDeal_status(dealStatus.getDeal_status());
            updatedDealStatus.setStatus(dealStatus.getStatus());
            updatedDealStatus.setUpdatedAt(LocalDateTime.now()); // Set the updatedAt timestamp
            return dealStatusRepository.save(updatedDealStatus);
        }
        return null;
    }

    @Override
    public boolean deleteDealStatus(Long id) {
        if (dealStatusRepository.existsById(id)) {
            dealStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
