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
public class SubscriptionDTO {
 private Long id;
 private String planName;
 private Integer durationMonths;
 private BigDecimal price;
 private Boolean deleted;
 // Auditable fields
 private LocalDateTime createdAt;
 private String createdBy;
 private LocalDateTime modifiedAt;
 private String modifiedBy;

}

