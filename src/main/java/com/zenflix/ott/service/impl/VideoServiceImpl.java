package com.zenflix.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zenflix.ott.dto.VideoDTO;
import com.zenflix.ott.entity.Genre;
import com.zenflix.ott.entity.Video;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.VideoMapper;
import com.zenflix.ott.repository.GenreRepository;
import com.zenflix.ott.repository.VideoRepository;
import com.zenflix.ott.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
	private final GenreRepository genreRepository;

    public VideoServiceImpl(VideoRepository videoRepository,GenreRepository genreRepository, VideoMapper videoMapper) {
        this.videoRepository = videoRepository;
        this.genreRepository = genreRepository;
        this.videoMapper = videoMapper;
    }

    @Override
    public VideoDTO createVideo(VideoDTO videoDTO) {
        Genre genre = genreRepository.findById(videoDTO.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        Video video = videoMapper.toEntity(videoDTO, genre);
        Video savedVideo = videoRepository.save(video);
        return videoMapper.toDTO(savedVideo);
    }

    @Override
    public VideoDTO getVideoById(Long id) {
        Video video = videoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        return videoMapper.toDTO(video);
    }

    @Override
    public List<VideoDTO> getAllVideos() {
        return videoRepository.findAllByDeletedFalse().stream()
            .map(videoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<VideoDTO> getVideosByGenre(Long genreId) {
        List<Video> videos = videoRepository.findByGenreIdAndDeletedFalse(genreId);
        return videos.stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VideoDTO updateVideo(Long id, VideoDTO videoDTO) {
        Video existingVideo = videoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        existingVideo.setTitle(videoDTO.getTitle());
        existingVideo.setDescription(videoDTO.getDescription());
        existingVideo.setUrl(videoDTO.getUrl());
        existingVideo.setIsPublicTrailer(videoDTO.getIsPublicTrailer());
        Video updatedVideo = videoRepository.save(existingVideo);
        return videoMapper.toDTO(updatedVideo);
    }

    @Override
    public void deleteVideo(Long id) {
        Video video = videoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        video.setDeleted(true);
        videoRepository.save(video);
    }
    
    @Override
    public List<VideoDTO> getPublicTrailers() {
        return videoRepository.findByIsPublicTrailerTrue()
                .stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
