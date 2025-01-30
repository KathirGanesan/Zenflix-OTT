package com.zenflix.ott.service.impl;

import com.zenflix.ott.dto.SubscriptionPlanDTO;
import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.SubscriptionPlanMapper;
import com.zenflix.ott.repository.SubscriptionPlanRepository;
import com.zenflix.ott.service.SubscriptionPlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionPlanMapper subscriptionPlanMapper) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
    }

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        SubscriptionPlan savedSubscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return subscriptionPlanMapper.toDTO(savedSubscriptionPlan);
    }

    @Override
    public SubscriptionPlanDTO getSubscriptionPlanById(Long id) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan not found"));
        return subscriptionPlanMapper.toDTO(subscriptionPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAllByDeletedFalse().stream()
            .map(subscriptionPlanMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long id, SubscriptionPlanDTO subscriptionPlanDTO) {
        SubscriptionPlan existingSubscriptionPlan = subscriptionPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan not found"));
        existingSubscriptionPlan.setPlanName(subscriptionPlanDTO.getPlanName());
        existingSubscriptionPlan.setDurationMonths(subscriptionPlanDTO.getDurationMonths());
        existingSubscriptionPlan.setPrice(subscriptionPlanDTO.getPrice());
        SubscriptionPlan updatedSubscriptionPlan = subscriptionPlanRepository.save(existingSubscriptionPlan);
        return subscriptionPlanMapper.toDTO(updatedSubscriptionPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long id) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan not found"));
        subscriptionPlan.setDeleted(true);
        subscriptionPlanRepository.save(subscriptionPlan);
    }

}
