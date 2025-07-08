package com.portal.bid.service;

import com.portal.bid.entity.BusinessUnit;
import com.portal.bid.entity.DealStatus;

import java.util.List;

public interface DealStatusService {

    DealStatus createDealStatus(DealStatus dealStatus);

    DealStatus getDealStatusById(Long id);

    List<DealStatus> getAllDealStatus();

    DealStatus updateDealStatus(Long id, DealStatus dealStatus);

    boolean deleteDealStatus(Long id);
    public List<DealStatus> getAllDealStatus1(Long formId);
}
