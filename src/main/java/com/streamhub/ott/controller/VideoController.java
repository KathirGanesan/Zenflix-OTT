package com.streamhub.ott.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamhub.ott.dto.VideoDTO;
import com.streamhub.ott.security.RequiresSubscription;
import com.streamhub.ott.service.VideoService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can add videos
    public ResponseEntity<VideoDTO> createVideo(@Valid @RequestBody VideoDTO videoDTO) {
        VideoDTO createdVideo = videoService.createVideo(videoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdVideo);
    }

    @GetMapping("/{id}")
    @RequiresSubscription
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Accessible by both users and admins
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable Long id) {
        VideoDTO video = videoService.getVideoById(id);
        return ResponseEntity.ok(video);
    }

    @GetMapping
    @RequiresSubscription
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Accessible by both users and admins
    public ResponseEntity<List<VideoDTO>> getAllVideos() {
        List<VideoDTO> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can update videos
    public ResponseEntity<VideoDTO> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoDTO videoDTO
    ) {
        VideoDTO updatedVideo = videoService.updateVideo(id, videoDTO);
        return ResponseEntity.ok(updatedVideo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete videos
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/trailers")
    public ResponseEntity<List<VideoDTO>> getPublicTrailers() {
        List<VideoDTO> publicTrailers = videoService.getPublicTrailers();
        return ResponseEntity.ok(publicTrailers);
    }
}
