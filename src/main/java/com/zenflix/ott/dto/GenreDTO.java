package com.zenflix.ott.dto;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
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
public class GenreDTO {
	
    private Long id;
    
    @NotBlank(message = "Genre name cannot be blank")
    @Size(max = 100, message = "Genre name must not exceed 100 characters")
    private String name;

    private Boolean deleted;
    // Auditable fields
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
