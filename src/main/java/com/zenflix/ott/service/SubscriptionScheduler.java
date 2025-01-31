package com.zenflix.ott.service;

import com.zenflix.ott.entity.UserSubscription;
import com.zenflix.ott.enums.SubscriptionStatus;
import com.zenflix.ott.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriptionScheduler {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Scheduled(cron = "0 * * * * ?") // runs at every minute
    public void updateSubscriptions() {
        // 1. Expire outdated ACTIVE subscriptions
        expireOutdatedSubscriptions();

        // 2. Activate queued subscriptions if start date is reached
        activateQueuedSubscriptions();
    }

    private void expireOutdatedSubscriptions() {
        LocalDateTime now = LocalDateTime.now();

        // Fetch all subscriptions that are ACTIVE
        List<UserSubscription> activeSubscriptions = userSubscriptionRepository
                .findBySubscriptionStatus(SubscriptionStatus.ACTIVE);

        for (UserSubscription subscription : activeSubscriptions) {
            // If it's end date is in the past (or equals now)
            if (subscription.getEndDate().isBefore(now) || subscription.getEndDate().isEqual(now)) {
                // Mark it as expired
                subscription.setSubscriptionStatus(SubscriptionStatus.EXPIRED);
                userSubscriptionRepository.save(subscription);
            }
        }
    }

    private void activateQueuedSubscriptions() {
        LocalDateTime now = LocalDateTime.now();

        // We could fetch all queued subscriptions for all users
        // But to handle "one active at a time" per user, we might do a user-by-user approach
        // This example does a simpler approach, but you can refine as needed

        // 1. We'll get all queued subscriptions, then group them by user

        List<UserSubscription> queuedSubscriptions = userSubscriptionRepository
                .findBySubscriptionStatus(SubscriptionStatus.QUEUED);

        // Group by user
        Map<Long, List<UserSubscription>> queuedByUser = queuedSubscriptions.stream()
                .collect(Collectors.groupingBy(us -> us.getUser().getId()));

        // 2. For each user, pick the earliest queued subscription that is ready to become active
        for (Map.Entry<Long, List<UserSubscription>> entry : queuedByUser.entrySet()) {
            Long userId = entry.getKey();
            List<UserSubscription> userQueuedSubs = entry.getValue();

            // Sort by startDate ascending
            userQueuedSubs.sort(Comparator.comparing(UserSubscription::getStartDate));

            // Check if user has an active subscription right now
            // (If you *already* expired outdated ones, you only check if there's currently an ACTIVE that hasn't ended)
            boolean hasActive = userSubscriptionRepository.existsByUser_IdAndSubscriptionStatus(userId, SubscriptionStatus.ACTIVE);
            if (hasActive) {
                // This user still has an active subscription, skip for now
                continue;
            }

            // Otherwise, we can activate the first queued sub that has startDate <= now
            for (UserSubscription queuedSub : userQueuedSubs) {
                if (!queuedSub.getStartDate().isAfter(now)) {
                    // Start date is now or in the past -> activate it
                    queuedSub.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                    userSubscriptionRepository.save(queuedSub);
                    break; // only activate the first one
                }
            }
        }
    }
}

