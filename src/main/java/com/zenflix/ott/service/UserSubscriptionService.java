package com.zenflix.ott.service;

import java.util.List;

import com.zenflix.ott.dto.UserSubscriptionDTO;

public interface UserSubscriptionService {
    UserSubscriptionDTO userSubscribe(UserSubscriptionDTO userSubscriptionDTO);
    UserSubscriptionDTO getUserSubscriptionById(Long id);
    List<UserSubscriptionDTO> getAllUserSubscriptions();
    void unsubscribe(Long userId, Long subscriptionId);
    UserSubscriptionDTO toggleAutoRenew(Long id);
}
