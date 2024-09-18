package com.portal.bid.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.bid.entity.PlanAction;
import com.portal.bid.repository.PlanActionRepository;
import com.portal.bid.service.PlanActionService;

@Service
public class PlanActionImp implements PlanActionService {

    @Autowired
    private PlanActionRepository planRepository;

    @Override
    public PlanAction createPlan(PlanAction plan) {
        plan.setCreatedAt(LocalDateTime.now());
        return planRepository.save(plan);
    }

    @Override
    public PlanAction updatePlan(Long id, PlanAction plan) {
        Optional<PlanAction> existingPlan = planRepository.findById(id);
        if (existingPlan.isPresent()) {
            PlanAction updatedPlan = existingPlan.get();
            updatedPlan.setAction(plan.getAction());
            updatedPlan.setUpdatedAt(LocalDateTime.now());
            return planRepository.save(updatedPlan);
        }
        return null;
    }

    @Override
    public PlanAction getPlanById(Long id) {
        return planRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    @Override
    public List<PlanAction> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public List<PlanAction> getAllPlansByFormId(Long formId) {
        return planRepository.findAll().stream()
                .filter(plan -> plan.getFormId().equals(formId))
                .collect(Collectors.toList());
    }
}
