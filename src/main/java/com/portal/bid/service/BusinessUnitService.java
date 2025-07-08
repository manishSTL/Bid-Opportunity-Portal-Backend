package com.portal.bid.service;

import com.portal.bid.entity.BusinessUnit;

import java.util.List;

public interface BusinessUnitService {
    BusinessUnit createBusinessUnit(BusinessUnit businessUnit);
    List<BusinessUnit> getAllBusinessUnits();
    BusinessUnit updateBusinessUnit(Long id, BusinessUnit businessUnit);
    boolean deleteBusinessUnit(Long id);

    BusinessUnit getBusinessUnitById(Long id);
}
