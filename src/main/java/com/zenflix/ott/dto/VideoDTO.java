package com.zenflix.ott.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class VideoDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "URL is required")
    @Size(max = 255, message = "URL must not exceed 255 characters")
    @Pattern(
        regexp = "^(http:\\/\\/|https:\\/\\/)?([a-zA-Z0-9-_]+\\.)+[a-zA-Z]{2,}(:[0-9]{1,5})?(\\/.*)?$",
        message = "Invalid URL format"
    )
    private String url;

    @NotNull(message = "Genre ID is required")
    private Long genreId;

    @NotNull(message = "Public trailer status must be specified")
    private Boolean isPublicTrailer;

    private Boolean deleted;
    
    // Auditable fields
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}