package com.portal.bid.service.implementation;

import com.portal.bid.entity.Form;
import com.portal.bid.repository.FormRepository;
import com.portal.bid.service.OpportunityService;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OpportunityServiceImp implements OpportunityService {

    @Autowired
    private FormRepository formRespository;
    @Override
    public Form saveOpportunity(Form f) {
        return formRespository.save(f);
    }

    @Override
    public List<Form> getAllOpportunities() {
        return formRespository.findAll();
    }

    @Override
    public Form getOpportunityById(Long id) {
        return formRespository.findById(id).orElse(null);
    }

    @Override
    public List<Form> getFilteredOpportunities(String status, String priority,String ob_fy,  String businessUnit,
                                               String industrySegment, LocalDate startDate, LocalDate endDate,
                                               String responsiblePerson, String customer,
                                               BigDecimal dealValueMin, BigDecimal dealValueMax) {
        List<Form> allOpportunities = getAllOpportunities();

        return allOpportunities.stream()
                .peek(opportunity -> System.out.println("date: " + opportunity.getSubmissionDate())) // Print dealStatus for debugging
                .filter(opportunity -> {
                    if (status != null) {
                        String dealStatus = opportunity.getDealStatus();
                        if (dealStatus != null) {
                            // Trim both strings before comparing
                            String trimmedDealStatus = dealStatus.trim();
                            String trimmedStatus = status.trim();
                            System.out.println("Comparing dealStatus: " + trimmedDealStatus + " with status: " + trimmedStatus);
                            System.out.println(trimmedDealStatus.equalsIgnoreCase(trimmedStatus)); // Print the result of equalsIgnoreCase
                            return trimmedDealStatus.equalsIgnoreCase(trimmedStatus);
                        }
                    }
                    return true; // Handle case where status is null or dealStatus is null
                })
                .filter(opportunity -> {
                    if (priority != null) {
                        String oppPriority = opportunity.getPriority();
                        if (oppPriority != null) {
                            String trimmedPriority = oppPriority.trim();
                            String trimmedFilterPriority = priority.trim();
                            System.out.println("Comparing priority: " + trimmedPriority + " with priority: " + trimmedFilterPriority);
                            boolean result = trimmedPriority.equalsIgnoreCase(trimmedFilterPriority);
                            System.out.println("Comparison result: " + result);
                            return result;
                        }
                    }
                    return true;
                }).filter(opportunity -> {
                    if (ob_fy != null) {
                        String oppPriority = opportunity.getObFy();
                        if (oppPriority != null) {
                            String trimmedPriority = oppPriority.trim();
                            String trimmedFilterPriority = ob_fy.trim();
                            System.out.println("Comparing priority: " + trimmedPriority + " with priority: " + trimmedFilterPriority);
                            boolean result = trimmedPriority.equalsIgnoreCase(trimmedFilterPriority);
                            System.out.println("Comparison result: " + result);
                            return result;
                        }
                    }
                    return true;
                })
                .filter(opportunity -> {
                    if (businessUnit != null) {
                        String oppBusinessUnit = opportunity.getBusinessUnit();
                        if (oppBusinessUnit != null) {
                            String trimmedBusinessUnit = oppBusinessUnit.trim();
                            String trimmedFilterBusinessUnit = businessUnit.trim();
                            System.out.println("Comparing businessUnit: " + trimmedBusinessUnit + " with businessUnit: " + trimmedFilterBusinessUnit);
                            boolean result = trimmedBusinessUnit.equalsIgnoreCase(trimmedFilterBusinessUnit);
                            System.out.println("Comparison result: " + result);
                            return result;
                        }
                    }
                    return true;
                })
                .filter(opportunity -> {
                    if (industrySegment != null) {
                        String oppIndustrySegment = opportunity.getIndustrySegment();
                        if (oppIndustrySegment != null) {
                            String trimmedIndustrySegment = oppIndustrySegment.trim();
                            String trimmedFilterIndustrySegment = industrySegment.trim();
                            System.out.println("Comparing industrySegment: " + trimmedIndustrySegment + " with industrySegment: " + trimmedFilterIndustrySegment);
                            boolean result = trimmedIndustrySegment.equalsIgnoreCase(trimmedFilterIndustrySegment);
                            System.out.println("Comparison result: " + result);
                            return result;
                        }
                    }
                    return true;
                })
                .filter(opportunity -> {
                    LocalDate leadReceivedDate = opportunity.getLeadReceivedDate();
                    if(startDate==null){
                        return true;
                    }
                    if (leadReceivedDate != null && startDate != null) {
                        System.out.println("Comparing leadReceivedDate: " + leadReceivedDate + " with startDate: " + startDate);
                        boolean result = !leadReceivedDate.isBefore(startDate);
                        System.out.println("Comparison result: " + result);
                        return result;
                    }
                    return false;
                })

                .filter(opportunity -> {
                    System.out.println("dkckjdncjfd");
                    LocalDate submissionDate = opportunity.getSubmissionDate();

                    if (endDate != null && submissionDate != null) {
                        System.out.println("Comparing submissionDate: " + submissionDate + " with endDate: " + endDate);
                        boolean result = !submissionDate.isAfter(endDate) || submissionDate.isEqual(endDate);
                        System.out.println("Comparison result: " + result);
                        return result;
                    }
                    return true; // Include opportunities where endDate is null or submissionDate is null
                })

                .filter(opportunity -> {
                    if (responsiblePerson != null) {
                        String primaryOwner = opportunity.getPrimaryOwner();
                        if (primaryOwner != null) {
                            String trimmedPrimaryOwner = primaryOwner.trim();
                            String trimmedFilterResponsiblePerson = responsiblePerson.trim();
                            System.out.println("Comparing primaryOwner: " + trimmedPrimaryOwner + " with responsiblePerson: " + trimmedFilterResponsiblePerson);
                            boolean result = trimmedPrimaryOwner.equalsIgnoreCase(trimmedFilterResponsiblePerson);
                            System.out.println("Comparison result: " + result);
                            return result;
                        }
                    }
                    return true;
                })
                .filter(opportunity -> {
                    if (customer != null) {
                        String customerAlignment = opportunity.getCustomerAlignment();
                        if (customerAlignment != null) {
                            String trimmedCustomerAlignment = customerAlignment.trim();
                            String trimmedFilterCustomer = customer.trim();
                            System.out.println("Comparing customerAlignment: " + trimmedCustomerAlignment + " with customer: " + trimmedFilterCustomer);
                            boolean result = trimmedCustomerAlignment.equalsIgnoreCase(trimmedFilterCustomer);
                            System.out.println("Comparison result: " + result);
                            return result;
                        }
                    }
                    return true;
                })
                .filter(opportunity -> {
                    if (dealValueMin != null) {
                        BigDecimal amountInrCrMax = opportunity.getAmountInrCrMax();
                        System.out.println("Comparing amountInrCrMax: " + amountInrCrMax + " with dealValueMin: " + dealValueMin);
                        boolean result = amountInrCrMax != null && amountInrCrMax.compareTo(dealValueMin) >= 0;
                        System.out.println("Comparison result: " + result);
                        return result;
                    }
                    return true;
                })
                .filter(opportunity -> {
                    if (dealValueMax != null) {
                        BigDecimal amountInrCrMax = opportunity.getAmountInrCrMax();
                        System.out.println("Comparing amountInrCrMax: " + amountInrCrMax + " with dealValueMax: " + dealValueMax);
                        boolean result = amountInrCrMax != null && amountInrCrMax.compareTo(dealValueMax) <= 0;
                        System.out.println("Comparison result: " + result);
                        return result;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Form updateOpportunity(Long id, Form updatedOpportunity) {
//        return formRespository.findById(id)
//                .map(existingOpportunity -> {
//                    // Update the fields of the existing opportunity with the new data
//                    if(updatedOpportunity.getGoNoGoDate()!=null){
//                        existingOpportunity.setGoNoGoDate(updatedOpportunity.getGoNoGoDate());
//                    }
//                    if(updatedOpportunity.getGoNoGoStatus()!=null){
//                        existingOpportunity.setGoNoGoStatus(updatedOpportunity.getGoNoGoStatus());
//                    }
//                    if(updatedOpportunity.getDealStatus()!=null){
//                        existingOpportunity.setDealStatus(updatedOpportunity.getDealStatus());
//                    }
//                    if(updatedOpportunity.getAmountInrCrMax()!=null){
//                        existingOpportunity.setAmountInrCrMax(updatedOpportunity.getAmountInrCrMax());
//                    }
//                    if(updatedOpportunity.getObQtr()!=null){
//                        existingOpportunity.setObQtr(updatedOpportunity.getObQtr());
//                    }
//                    existingOpportunity.setAdditionalRemarks(updatedOpportunity.getAdditionalRemarks());
//
//                    // Save the updated opportunity back to the repository
//                    return formRespository.save(existingOpportunity);
//                })
//                .orElse(null); // Return null if the opportunity with the given ID does not exist

        return formRespository.findById(id).map(existingOpportunity -> {
            // Get all declared fields from the Form class
            Field[] fields = Form.class.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true); // Allows access to private fields
                try {
                    // Get the value of the field in the updatedOpportunity object
                    Object updatedValue = field.get(updatedOpportunity);

                    // If the updated value is not null, set it on the existingOpportunity object
                    if (Objects.nonNull(updatedValue)) {
                        field.set(existingOpportunity, updatedValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to update field: " + field.getName(), e);
                }
            }

            // Save the updated opportunity back to the repository
            return formRespository.save(existingOpportunity);
        }).orElse(null);
    }

    public List<Form> findPotentialDuplicates(Form newOpportunity) {
        // Step 1: Query the database for potential matches based on key fields
        List<Form> potentialMatches = formRespository.findPotentialDuplicates(
                newOpportunity.getOpportunity(),
                newOpportunity.getIndustrySegment(),
                newOpportunity.getBusinessUnit(),
                newOpportunity.getSubmissionDate()
        );

        // Step 2: Further filter the potential matches using more detailed criteria
        return potentialMatches.stream()
                .filter(existingOpp -> isDuplicate(newOpportunity, existingOpp))
                .collect(Collectors.toList());
    }

    private boolean isDuplicate(Form newOpp, Form existingOpp) {
        // Check for exact matches on key fields
        if (Objects.equals(newOpp.getOpportunity(), existingOpp.getOpportunity()) &&
                Objects.equals(newOpp.getIndustrySegment(), existingOpp.getIndustrySegment()) &&
                Objects.equals(newOpp.getBusinessUnit(), existingOpp.getBusinessUnit()) &&
                Objects.equals(newOpp.getSubmissionDate(), existingOpp.getSubmissionDate())) {
            return true;
        }

        // Check for high similarity in opportunity title using Levenshtein distance
        if (LevenshteinDistance.getDefaultInstance().apply(
                newOpp.getOpportunity(), existingOpp.getOpportunity()) <= 3) {
            return true;
        }

        // Check for matches in multiple fields
        int matchCount = 0;
        if (Objects.equals(newOpp.getOpportunityType(), existingOpp.getOpportunityType())) matchCount++;
        if (Objects.equals(newOpp.getAmountInrCrMin(), existingOpp.getAmountInrCrMin())) matchCount++;
        if (Objects.equals(newOpp.getAmountInrCrMax(), existingOpp.getAmountInrCrMax())) matchCount++;
        if (Objects.equals(newOpp.getPrimaryOfferingSegment(), existingOpp.getPrimaryOfferingSegment())) matchCount++;
        if (Objects.equals(newOpp.getSecondaryOfferingSegment(), existingOpp.getSecondaryOfferingSegment())) matchCount++;

        // Consider it a potential duplicate if 3 or more fields match
        return matchCount >= 3;
    }


}
