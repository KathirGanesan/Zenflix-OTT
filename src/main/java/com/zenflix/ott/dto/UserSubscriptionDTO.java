package com.zenflix.ott.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString	
public class UserSubscriptionDTO {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "SubscriptionPlan ID is required")
    private Long subscriptionId;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextRenewalDate;
    private BigDecimal paymentAmount;
    private String transactionReferenceId;
    private String paymentStatusMessage;
    private String paymentLink;
    private String subscriptionStatus;
    // Auditable fields
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
