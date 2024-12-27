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
public class PaymentDTO {
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private BigDecimal amount;
    private String paymentStatus;
    private LocalDateTime transactionDate;
    private String transactionId;


}
