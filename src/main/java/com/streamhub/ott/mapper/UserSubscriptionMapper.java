package com.streamhub.ott.mapper;

import org.springframework.stereotype.Component;

import com.streamhub.ott.dto.UserSubscriptionDTO;
import com.streamhub.ott.entity.Subscription;
import com.streamhub.ott.entity.User;
import com.streamhub.ott.entity.UserSubscription;

@Component
public class UserSubscriptionMapper {
    public UserSubscriptionDTO toDTO(UserSubscription userSubscription) {
        if (userSubscription == null) return null;
        UserSubscriptionDTO dto = new UserSubscriptionDTO();
        dto.setId(userSubscription.getId());
        dto.setUserId(userSubscription.getUser().getId());
        dto.setSubscriptionId(userSubscription.getSubscription().getId());
        dto.setStartDate(userSubscription.getStartDate());
        dto.setEndDate(userSubscription.getEndDate());
        dto.setNextRenewalDate(userSubscription.getNextRenewalDate());
        dto.setIsAutoRenew(userSubscription.getIsAutoRenew());
        return dto;
    }

    public UserSubscription toEntity(UserSubscriptionDTO dto, User user, Subscription subscription) {
        if (dto == null) return null;
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setId(dto.getId());
        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setStartDate(dto.getStartDate());
        userSubscription.setEndDate(dto.getEndDate());
        userSubscription.setNextRenewalDate(dto.getNextRenewalDate());
        userSubscription.setIsAutoRenew(dto.getIsAutoRenew());
        return userSubscription;
    }
}
