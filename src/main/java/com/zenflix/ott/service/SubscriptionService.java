package com.zenflix.ott.service;

import java.util.List;

import com.zenflix.ott.dto.SubscriptionDTO;

public interface SubscriptionService {
    SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO);
    SubscriptionDTO getSubscriptionById(Long id);
    List<SubscriptionDTO> getAllSubscriptions();
    SubscriptionDTO updateSubscription(Long id, SubscriptionDTO subscriptionDTO);
    void deleteSubscription(Long id);
}