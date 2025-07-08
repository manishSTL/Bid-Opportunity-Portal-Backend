package com.portal.bid.service;

import com.portal.bid.entity.Deal;
import com.portal.bid.entity.GoNoGoMaster;

import java.util.List;

public interface GoNoGoMasterService{
    GoNoGoMaster createDeal(GoNoGoMaster deal);
    List<GoNoGoMaster> getAllDeals();
    GoNoGoMaster getDealById(Integer id);
    GoNoGoMaster updateDeal(Integer id, GoNoGoMaster deal);
    boolean deleteDeal(Integer id);
}
