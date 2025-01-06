package com.zenflix.ott.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString	
public class UserSubscriptionDTO {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Subscription ID is required")
    private Long subscriptionId;
    
    @NotNull(message = "Auto-renew status must be specified")
    private Boolean isAutoRenew;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextRenewalDate;
    private BigDecimal paymentAmount;
    private String transactionId;
    private String paymentStatusMessage; 
    // Auditable fields
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
