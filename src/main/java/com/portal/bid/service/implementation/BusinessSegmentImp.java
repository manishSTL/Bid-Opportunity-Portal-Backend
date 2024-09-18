package com.portal.bid.service.implementation;
import com.portal.bid.entity.BusinessSegment;
import com.portal.bid.repository.BusinessSegmentRepository;
import com.portal.bid.service.BusinessSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessSegmentImp implements BusinessSegmentService {

    @Autowired
    private BusinessSegmentRepository businessSegmentRepository;

    @Override
    public BusinessSegment createBusinessSegment(BusinessSegment b) {
        return businessSegmentRepository.save(b);
    }

    @Override
    public List<BusinessSegment> getAllBusinessSegments() {
        return businessSegmentRepository.findAll();

    }

    @Override
    public BusinessSegment updateBusinessSegment(Long id, BusinessSegment businessSegment) {
        Optional<BusinessSegment> existingSegment = businessSegmentRepository.findById(id);
        if (existingSegment.isPresent()) {
            businessSegment.setId(id);
            return businessSegmentRepository.save(businessSegment);
        }
        return null;
    }

    @Override
    public boolean deleteBusinessSegment(Long id) {
        if (businessSegmentRepository.existsById(id)) {
            businessSegmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
