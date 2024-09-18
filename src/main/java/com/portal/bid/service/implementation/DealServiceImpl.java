package com.portal.bid.service.implementation;

import com.portal.bid.entity.Deal;
import com.portal.bid.repository.DealRepository;
import com.portal.bid.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealRepository dealRepository;

    @Override
    public Deal createDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal updateDeal(Integer id, Deal dealDetails) {
        Optional<Deal> existingDealOptional = dealRepository.findById(id);
        if (existingDealOptional.isPresent()) {
            Deal existingDeal = existingDealOptional.get();

            // Update only fields that are not null in the incoming dealDetails
            if (dealDetails.getDealStatus() != null) {
                existingDeal.setDealStatus(dealDetails.getDealStatus());
            }
            if (dealDetails.getStatus() != null) {
                existingDeal.setStatus(dealDetails.getStatus());
            }
            // Ensure updatedAt is always updated
            existingDeal.setUpdatedAt(LocalDateTime.now());

            // Save and return the updated entity
            return dealRepository.save(existingDeal);
        }
        return null;
    }

    @Override
    public boolean deleteDeal(Integer id) {
        if (dealRepository.existsById(id)) {
            dealRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
