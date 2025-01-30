package com.zenflix.ott.service;

import com.zenflix.ott.dto.UserSubscriptionDTO;

import java.util.List;

public interface UserSubscriptionService {
    UserSubscriptionDTO userSubscribe(UserSubscriptionDTO userSubscriptionDTO);
    UserSubscriptionDTO getUserSubscriptionById(Long id);
    List<UserSubscriptionDTO> getAllUserSubscriptions();
    void unsubscribe(Long userId, Long subscriptionId);
    UserSubscriptionDTO toggleAutoRenew(Long id);
    void activateSubscriptionByTransactionReference(String transactionReferenceId);
}
