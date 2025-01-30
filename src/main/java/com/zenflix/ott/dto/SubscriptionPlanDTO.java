package com.zenflix.ott.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class SubscriptionPlanDTO {
	
	 private Long id;

	    @NotBlank(message = "Plan name cannot be blank")
	    @Size(max = 100, message = "Plan name must not exceed 100 characters")
	    private String planName;

	    @NotNull(message = "Duration in months is required")
	    @Min(value = 1, message = "Duration must be at least 1 month")
	    @Max(value = 120, message = "Duration must not exceed 120 months")
	    private Integer durationMonths;

	    @NotNull(message = "Price is required")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	    @Digits(integer = 10, fraction = 2, message = "Price must be a valid decimal with up to 2 decimal places")
	    private BigDecimal price;

	    private Boolean deleted;
 // Auditable fields
 private LocalDateTime createdAt;
 private String createdBy;
 private LocalDateTime modifiedAt;
 private String modifiedBy;

}

