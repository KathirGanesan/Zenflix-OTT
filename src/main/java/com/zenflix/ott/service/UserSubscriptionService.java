package com.zenflix.ott.service;

import com.zenflix.ott.dto.UserSubscriptionDTO;

import java.util.List;

public interface UserSubscriptionService {
    UserSubscriptionDTO userSubscribe(UserSubscriptionDTO userSubscriptionDTO);
    UserSubscriptionDTO getUserSubscriptionById(Long id);
    List<UserSubscriptionDTO> getActiveAndQueuedUserSubscriptions();
    void unsubscribe(Long userId, Long userSubscriptionId);
    void activateSubscriptionByTransactionReference(String transactionReferenceId);
}
