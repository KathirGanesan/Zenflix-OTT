package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.VideoDTO;
import com.zenflix.ott.entity.Genre;
import com.zenflix.ott.entity.Video;

@Component
public class VideoMapper {
    public VideoDTO toDTO(Video video) {
        if (video == null) {
            return null;
        }
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(video.getId());
        videoDTO.setTitle(video.getTitle());
        videoDTO.setDescription(video.getDescription());
        videoDTO.setGenreId(video.getGenre() != null ? video.getGenre().getId() : null);
        videoDTO.setIsPublicTrailer(video.getIsPublicTrailer());
        videoDTO.setUrl(video.getUrl());
        return videoDTO;
    }

    public Video toEntity(VideoDTO videoDTO, Genre genre) {
        if (videoDTO == null) {
            return null;
        }
        Video video = new Video();
        video.setId(videoDTO.getId());
        video.setTitle(videoDTO.getTitle());
        video.setDescription(videoDTO.getDescription());
        video.setGenre(genre);
        video.setIsPublicTrailer(videoDTO.getIsPublicTrailer());
        video.setUrl(videoDTO.getUrl());
        video.setDeleted(false);
        return video;
    }
}
