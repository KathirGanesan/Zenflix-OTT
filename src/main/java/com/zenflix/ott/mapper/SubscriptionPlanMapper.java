package com.zenflix.ott.mapper;

import com.zenflix.ott.entity.SubscriptionPlan;
import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.SubscriptionPlanDTO;

//SubscriptionPlanMapper
@Component
public class SubscriptionPlanMapper {
 public SubscriptionPlanDTO toDTO(SubscriptionPlan subscriptionPlan) {
     if (subscriptionPlan == null) return null;
     SubscriptionPlanDTO subscriptionPlanDTO = new SubscriptionPlanDTO();
     subscriptionPlanDTO.setId(subscriptionPlan.getId());
     subscriptionPlanDTO.setPlanName(subscriptionPlan.getPlanName());
     subscriptionPlanDTO.setDurationMonths(subscriptionPlan.getDurationMonths());
     subscriptionPlanDTO.setPrice(subscriptionPlan.getPrice());
     subscriptionPlanDTO.setDeleted(subscriptionPlan.getDeleted());
     // Map auditable fields
     subscriptionPlanDTO.setCreatedAt(subscriptionPlan.getCreatedAt());
     subscriptionPlanDTO.setCreatedBy(subscriptionPlan.getCreatedBy());
     subscriptionPlanDTO.setModifiedAt(subscriptionPlan.getModifiedAt());
     subscriptionPlanDTO.setModifiedBy(subscriptionPlan.getModifiedBy());
     return subscriptionPlanDTO;
 }

 public SubscriptionPlan toEntity(SubscriptionPlanDTO dto) {
     if (dto == null) return null;
     SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
     subscriptionPlan.setId(dto.getId());
     subscriptionPlan.setPlanName(dto.getPlanName());
     subscriptionPlan.setDurationMonths(dto.getDurationMonths());
     subscriptionPlan.setPrice(dto.getPrice());
     subscriptionPlan.setDeleted(dto.getDeleted() != null ? dto.getDeleted() : false);
     return subscriptionPlan;
 }
}
