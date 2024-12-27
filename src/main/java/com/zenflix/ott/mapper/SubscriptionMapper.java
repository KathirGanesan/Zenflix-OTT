package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.SubscriptionDTO;
import com.zenflix.ott.entity.Subscription;

//SubscriptionMapper
@Component
public class SubscriptionMapper {
 public SubscriptionDTO toDTO(Subscription subscription) {
     if (subscription == null) return null;
     SubscriptionDTO dto = new SubscriptionDTO();
     dto.setId(subscription.getId());
     dto.setPlanName(subscription.getPlanName());
     dto.setDurationMonths(subscription.getDurationMonths());
     dto.setPrice(subscription.getPrice());
     dto.setDeleted(subscription.getDeleted());
     return dto;
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
