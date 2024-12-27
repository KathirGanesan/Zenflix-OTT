package com.zenflix.ott.dto;
import java.math.BigDecimal;

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

}

