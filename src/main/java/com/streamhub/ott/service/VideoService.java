package com.streamhub.ott.service;

import java.util.List;

import com.streamhub.ott.dto.VideoDTO;

public interface VideoService {
    VideoDTO createVideo(VideoDTO videoDTO);
    VideoDTO getVideoById(Long id);
    List<VideoDTO> getAllVideos();
    VideoDTO updateVideo(Long id, VideoDTO videoDTO);
    void deleteVideo(Long id);
	List<VideoDTO> getPublicTrailers();
}