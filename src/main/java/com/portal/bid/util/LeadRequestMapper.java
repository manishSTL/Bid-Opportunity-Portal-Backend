package com.portal.bid.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.bid.dto.LeadDTO;

/**
 * Utility class to map JSON request to LeadDTO
 */
public class LeadRequestMapper {

    /**
     * Maps a JSON request to LeadDTO
     * @param requestBody The JSON request body
     * @return Mapped LeadDTO
     */
    public static LeadDTO mapJsonToLeadDTO(String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);
            
            LeadDTO leadDTO = new LeadDTO();
            
            // Extract nested ID fields
            setLongValueIfPresent(rootNode, "partFy.id", leadDTO::setPartFyId);
            setLongValueIfPresent(rootNode, "obFy.id", leadDTO::setObFyId);
            setLongValueIfPresent(rootNode, "priority.id", leadDTO::setPriorityId);
            setLongValueIfPresent(rootNode, "dealStatus.id", leadDTO::setDealStatusId);
            setLongValueIfPresent(rootNode, "industrySegment.id", leadDTO::setIndustrySegmentId);
            setLongValueIfPresent(rootNode, "goNoGoMaster.id", leadDTO::setGoNoGoStatusId);
            
            // Extract direct fields
            setStringValueIfPresent(rootNode, "opportunityName", leadDTO::setOpportunityName);
            setStringValueIfPresent(rootNode, "partQuarter", leadDTO::setPartQuarter);
            setStringValueIfPresent(rootNode, "partMonth", leadDTO::setPartMonth);
            setStringValueIfPresent(rootNode, "obQtr", leadDTO::setObQtr);
            setStringValueIfPresent(rootNode, "obMmm", leadDTO::setObMmm);
            setStringValueIfPresent(rootNode, "publicPrivate", leadDTO::setPublicPrivate);
            setStringValueIfPresent(rootNode, "primaryOfferingSegment", leadDTO::setPrimaryOfferingSegment);
            setStringValueIfPresent(rootNode, "secondaryOfferingSegment", leadDTO::setSecondaryOfferingSegment);
            setStringValueIfPresent(rootNode, "primaryOwner", leadDTO::setPrimaryOwner);
            setStringValueIfPresent(rootNode, "solutionSpoc", leadDTO::setSolutionSpoc);
            setStringValueIfPresent(rootNode, "scmSpoc", leadDTO::setScmSpoc);
            setStringValueIfPresent(rootNode, "remarks", leadDTO::setRemarks);
            setStringValueIfPresent(rootNode, "pqTq_remarks", leadDTO::setPqTq_remarks);
            
            setIntegerValueIfPresent(rootNode, "probability", leadDTO::setProbability);
            setIntegerValueIfPresent(rootNode, "projectTenureMonths", leadDTO::setProjectTenureMonths);
            setIntegerValueIfPresent(rootNode, "opexTenureMonths", leadDTO::setOpexTenureMonths);
            
            setBigDecimalValueIfPresent(rootNode, "amount", leadDTO::setAmount);
            setBigDecimalValueIfPresent(rootNode, "actualBookedOb", leadDTO::setActualBookedOb);
            setBigDecimalValueIfPresent(rootNode, "actualBookedCapex", leadDTO::setActualBookedCapex);
            setBigDecimalValueIfPresent(rootNode, "actualBookedOpex", leadDTO::setActualBookedOpex);
            setBigDecimalValueIfPresent(rootNode, "revInObQtr", leadDTO::setRevInObQtr);
            setBigDecimalValueIfPresent(rootNode, "revInObQtrPlus1", leadDTO::setRevInObQtrPlus1);
            setBigDecimalValueIfPresent(rootNode, "estCapexInrCr", leadDTO::setEstCapexInrCr);
            setBigDecimalValueIfPresent(rootNode, "estOpexInrCr", leadDTO::setEstOpexInrCr);
            setBigDecimalValueIfPresent(rootNode, "gmPercentage", leadDTO::setGmPercentage);
            
            setLocalDateValueIfPresent(rootNode, "goNoGoDate", leadDTO::setGoNoGoDate);
            setLocalDateValueIfPresent(rootNode, "rfpReleaseDate", leadDTO::setRfpReleaseDate);
            setLocalDateValueIfPresent(rootNode, "bidSubmissionDate", leadDTO::setBidSubmissionDate);
            
            return leadDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to LeadDTO: " + e.getMessage(), e);
        }
    }
    
    private static void setLongValueIfPresent(JsonNode rootNode, String path, java.util.function.Consumer<Long> setter) {
        JsonNode node = getNestedNode(rootNode, path);
        if (node != null && !node.isNull()) {
            setter.accept(node.asLong());
        }
    }
    
    private static void setStringValueIfPresent(JsonNode rootNode, String fieldName, java.util.function.Consumer<String> setter) {
        if (rootNode.has(fieldName) && !rootNode.get(fieldName).isNull()) {
            setter.accept(rootNode.get(fieldName).asText());
        }
    }
    
    private static void setIntegerValueIfPresent(JsonNode rootNode, String fieldName, java.util.function.Consumer<Integer> setter) {
        if (rootNode.has(fieldName) && !rootNode.get(fieldName).isNull()) {
            setter.accept(rootNode.get(fieldName).asInt());
        }
    }
    
    private static void setBigDecimalValueIfPresent(JsonNode rootNode, String fieldName, java.util.function.Consumer<BigDecimal> setter) {
        if (rootNode.has(fieldName) && !rootNode.get(fieldName).isNull()) {
            setter.accept(new BigDecimal(rootNode.get(fieldName).asText()));
        }
    }
    
    private static void setLocalDateValueIfPresent(JsonNode rootNode, String fieldName, java.util.function.Consumer<LocalDate> setter) {
        if (rootNode.has(fieldName) && !rootNode.get(fieldName).isNull()) {
            setter.accept(LocalDate.parse(rootNode.get(fieldName).asText()));
        }
    }
    
    private static JsonNode getNestedNode(JsonNode rootNode, String path) {
        String[] parts = path.split("\\.");
        JsonNode currentNode = rootNode;
        
        for (String part : parts) {
            if (currentNode != null && currentNode.has(part)) {
                currentNode = currentNode.get(part);
            } else {
                return null;
            }
        }
        
        return currentNode;
    }
}