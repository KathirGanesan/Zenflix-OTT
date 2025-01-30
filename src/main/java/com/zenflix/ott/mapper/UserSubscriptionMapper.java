package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.UserSubscriptionDTO;
import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.UserSubscription;

@Component
public class UserSubscriptionMapper {
    public UserSubscriptionDTO toDTO(UserSubscription userSubscription) {
        if (userSubscription == null) return null;
        UserSubscriptionDTO dto = new UserSubscriptionDTO();
        dto.setId(userSubscription.getId());
        dto.setUserId(userSubscription.getUser().getId());
        dto.setSubscriptionId(userSubscription.getSubscriptionPlan().getId());
        dto.setStartDate(userSubscription.getStartDate());
        dto.setEndDate(userSubscription.getEndDate());
        dto.setNextRenewalDate(userSubscription.getNextRenewalDate());
        dto.setIsAutoRenew(userSubscription.getIsAutoRenew());
        // Map auditable fields
        dto.setCreatedAt(userSubscription.getCreatedAt());
        dto.setCreatedBy(userSubscription.getCreatedBy());
        dto.setModifiedAt(userSubscription.getModifiedAt());
        dto.setModifiedBy(userSubscription.getModifiedBy());
        return dto;
    }

    public UserSubscription toEntity(UserSubscriptionDTO dto, User user, SubscriptionPlan subscriptionPlan) {
        if (dto == null) return null;
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setId(dto.getId());
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setStartDate(dto.getStartDate());
        userSubscription.setEndDate(dto.getEndDate());
        userSubscription.setNextRenewalDate(dto.getNextRenewalDate());
        userSubscription.setIsAutoRenew(dto.getIsAutoRenew());
        return userSubscription;
    }
}
