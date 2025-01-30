package com.zenflix.ott.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionDTO {
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private BigDecimal amount;
    private String paymentStatus;
    private LocalDateTime transactionDate;
    private String transactionReferenceId;
    // Auditable fields
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

}
