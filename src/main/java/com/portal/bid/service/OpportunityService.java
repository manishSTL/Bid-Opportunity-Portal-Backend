package com.portal.bid.service;

import com.portal.bid.entity.Form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OpportunityService {

    Form saveOpportunity(Form opportunity);
    List<Form> getAllOpportunities();
    Form getOpportunityById(Long id);
    public Form updateOpportunity(Long id, Form updatedOpportunity);
    List<Form> getFilteredOpportunities(String status, String priority, String ob_fy, String businessUnit, String industrySegment, LocalDate startDate, LocalDate endDate, String responsiblePerson, String customer, BigDecimal dealValueMin, BigDecimal dealValueMax);
    public List<Form> findPotentialDuplicates(Form newOpportunity);
}
