package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.SubscriptionDTO;
import com.zenflix.ott.entity.Subscription;

//SubscriptionMapper
@Component
public class SubscriptionMapper {
 public SubscriptionDTO toDTO(Subscription subscription) {
     if (subscription == null) return null;
     SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
     subscriptionDTO.setId(subscription.getId());
     subscriptionDTO.setPlanName(subscription.getPlanName());
     subscriptionDTO.setDurationMonths(subscription.getDurationMonths());
     subscriptionDTO.setPrice(subscription.getPrice());
     subscriptionDTO.setDeleted(subscription.getDeleted());
     // Map auditable fields
     subscriptionDTO.setCreatedAt(subscription.getCreatedAt());
     subscriptionDTO.setCreatedBy(subscription.getCreatedBy());
     subscriptionDTO.setModifiedAt(subscription.getModifiedAt());
     subscriptionDTO.setModifiedBy(subscription.getModifiedBy());
     return subscriptionDTO;
 }

 public Subscription toEntity(SubscriptionDTO dto) {
     if (dto == null) return null;
     Subscription subscription = new Subscription();
     subscription.setId(dto.getId());
     subscription.setPlanName(dto.getPlanName());
     subscription.setDurationMonths(dto.getDurationMonths());
     subscription.setPrice(dto.getPrice());
     subscription.setDeleted(dto.getDeleted() != null ? dto.getDeleted() : false);
     return subscription;
 }
}
