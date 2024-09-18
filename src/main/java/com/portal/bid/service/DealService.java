package com.portal.bid.service;

import com.portal.bid.entity.Deal;

import java.util.List;

public interface DealService {
    Deal createDeal(Deal deal);
    List<Deal> getAllDeals();
    Deal updateDeal(Integer id, Deal deal);
    boolean deleteDeal(Integer id);
}
