package com.streamhub.ott.dto;

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
    private String title;
    private String description;
    private String url;
    private Long genreId;
    private Boolean isPublicTrailer;
}
