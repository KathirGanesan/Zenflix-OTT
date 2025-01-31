package com.zenflix.ott.service;

import com.zenflix.ott.dto.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);
    SubscriptionPlanDTO getSubscriptionPlanById(Long id);
    List<SubscriptionPlanDTO> getAllSubscriptionPlans();
    SubscriptionPlanDTO updateSubscriptionPlan(Long id, SubscriptionPlanDTO subscriptionPlanDTO);
    void deleteSubscriptionPlan(Long id);
}