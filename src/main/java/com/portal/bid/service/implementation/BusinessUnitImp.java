package com.portal.bid.service.implementation;

import com.portal.bid.entity.BusinessUnit;
import com.portal.bid.repository.BusinessUnitRepository;
import com.portal.bid.service.BusinessUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUnitImp implements BusinessUnitService {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Override
    public BusinessUnit createBusinessUnit(BusinessUnit businessUnit) {
        return businessUnitRepository.save(businessUnit);
    }

    @Override
    public List<BusinessUnit> getAllBusinessUnits() {
        return businessUnitRepository.findAll();
    }

    @Override
    public BusinessUnit updateBusinessUnit(Long id, BusinessUnit businessUnit) {
        Optional<BusinessUnit> existingUnit = businessUnitRepository.findById(id);
        if (existingUnit.isPresent()) {
            businessUnit.setId(id);
            return businessUnitRepository.save(businessUnit);
        }
        return null; // Or throw an exception
    }

    @Override
    public boolean deleteBusinessUnit(Long id) {
        if (businessUnitRepository.existsById(id)) {
            businessUnitRepository.deleteById(id);
            return true;
        }
        return false; // Or throw an exception
    }

    @Override
    public BusinessUnit getBusinessUnitById(Long id) {
        return  businessUnitRepository.findById(id).orElse(null);
    }
}
