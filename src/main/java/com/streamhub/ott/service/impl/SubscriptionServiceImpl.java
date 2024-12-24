package com.streamhub.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.streamhub.ott.dto.SubscriptionDTO;
import com.streamhub.ott.entity.Subscription;
import com.streamhub.ott.exception.ResourceNotFoundException;
import com.streamhub.ott.mapper.SubscriptionMapper;
import com.streamhub.ott.repository.SubscriptionRepository;
import com.streamhub.ott.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
    }

    @Override
    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAllByDeletedFalse().stream()
            .map(subscriptionMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SubscriptionDTO updateSubscription(Long id, SubscriptionDTO subscriptionDTO) {
        Subscription existingSubscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        existingSubscription.setPlanName(subscriptionDTO.getPlanName());
        existingSubscription.setDurationMonths(subscriptionDTO.getDurationMonths());
        existingSubscription.setPrice(subscriptionDTO.getPrice());
        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return subscriptionMapper.toDTO(updatedSubscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        subscription.setDeleted(true);
        subscriptionRepository.save(subscription);
    }

}
