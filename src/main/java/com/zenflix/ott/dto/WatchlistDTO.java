package com.zenflix.ott.dto;
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
public class WatchlistDTO {
    private Long id;
    private Long userId;
    private Long videoId;
}
