package com.portal.bid.service;

import java.util.List;

import com.portal.bid.entity.PlanAction;

public interface PlanActionService {

        PlanAction createPlan(PlanAction plan);
        PlanAction updatePlan(Long id, PlanAction plan);
        PlanAction getPlanById(Long id);
        void deletePlan(Long id);
        List<PlanAction> getAllPlans();
        public List<PlanAction> getAllPlansByFormId(Long formId);


}
