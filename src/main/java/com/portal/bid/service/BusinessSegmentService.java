package com.portal.bid.service;

import java.util.List;

import com.portal.bid.entity.BusinessSegment;

public interface BusinessSegmentService {
    BusinessSegment createBusinessSegment(BusinessSegment b);

    List<BusinessSegment> getAllBusinessSegments();

    BusinessSegment updateBusinessSegment(Long id, BusinessSegment businessSegment);

    boolean deleteBusinessSegment(Long id);

    BusinessSegment getbyid(Long id);
}
