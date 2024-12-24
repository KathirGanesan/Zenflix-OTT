package com.streamhub.ott.service;

import java.util.List;

import com.streamhub.ott.dto.UserSubscriptionDTO;

public interface UserSubscriptionService {
    UserSubscriptionDTO userSubscribe(UserSubscriptionDTO userSubscriptionDTO);
    UserSubscriptionDTO getUserSubscriptionById(Long id);
    List<UserSubscriptionDTO> getAllUserSubscriptions();
    void unsubscribe(Long id);
    UserSubscriptionDTO toggleAutoRenew(Long id);
}
