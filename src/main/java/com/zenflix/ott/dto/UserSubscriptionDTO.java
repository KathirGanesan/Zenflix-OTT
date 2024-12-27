package com.zenflix.ott.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Long userId;
    private Long subscriptionId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextRenewalDate;
    private Boolean isAutoRenew;
    private BigDecimal paymentAmount;
    private String transactionId;
    private String paymentStatusMessage; 
}
