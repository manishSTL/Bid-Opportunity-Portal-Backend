package com.portal.bid.service.implementation;

import com.portal.bid.entity.GoNoGoMaster;
import com.portal.bid.repository.GoNoGoMasterRepository;
import com.portal.bid.service.GoNoGoMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoNoGoMasterImpl implements GoNoGoMasterService {

    @Autowired
    private GoNoGoMasterRepository dealRepository;

    @Override
    public GoNoGoMaster createDeal(GoNoGoMaster deal) {
        return dealRepository.save(deal);
    }

    @Override
    public List<GoNoGoMaster> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public GoNoGoMaster getDealById(Integer id) {
        Optional<GoNoGoMaster> deal = dealRepository.findById(id);
        return deal.orElse(null);
    }

    @Override
    public GoNoGoMaster updateDeal(Integer id, GoNoGoMaster dealDetails) {
        Optional<GoNoGoMaster> existingDealOptional = dealRepository.findById(id);
        if (existingDealOptional.isPresent()) {
            GoNoGoMaster existingDeal = existingDealOptional.get();

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
