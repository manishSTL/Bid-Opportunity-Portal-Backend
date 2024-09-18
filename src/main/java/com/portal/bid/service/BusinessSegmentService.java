package com.portal.bid.service;

import com.portal.bid.entity.BusinessSegment;

import java.util.List;

public interface BusinessSegmentService {
    BusinessSegment createBusinessSegment(BusinessSegment b);

    List<BusinessSegment> getAllBusinessSegments();

    BusinessSegment updateBusinessSegment(Long id, BusinessSegment businessSegment);

    boolean deleteBusinessSegment(Long id);
}
