package com.streamhub.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.streamhub.ott.dto.WatchlistDTO;
import com.streamhub.ott.entity.User;
import com.streamhub.ott.entity.Video;
import com.streamhub.ott.entity.Watchlist;
import com.streamhub.ott.exception.ResourceNotFoundException;
import com.streamhub.ott.mapper.WatchlistMapper;
import com.streamhub.ott.repository.UserRepository;
import com.streamhub.ott.repository.VideoRepository;
import com.streamhub.ott.repository.WatchlistRepository;
import com.streamhub.ott.service.WatchlistService;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final WatchlistMapper watchlistMapper;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository, UserRepository userRepository, VideoRepository videoRepository, WatchlistMapper watchlistMapper) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistMapper = watchlistMapper;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public WatchlistDTO addVideoToWatchlist(WatchlistDTO watchlistDTO) {
        User user = userRepository.findById(watchlistDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Video video = videoRepository.findById(watchlistDTO.getVideoId())
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));

        // Check if video is already in the user's watchlist
        boolean alreadyInWatchlist = watchlistRepository.existsByUserAndVideo(user, video);
        if (alreadyInWatchlist) {
            throw new IllegalArgumentException("Video is already in the watchlist");
        }

        // Map and save the watchlist entry
        Watchlist watchlist = watchlistMapper.toEntity(watchlistDTO, user, video);
        Watchlist savedWatchlist = watchlistRepository.save(watchlist);

        return watchlistMapper.toDTO(savedWatchlist);
    }


    @Override
    public List<WatchlistDTO> getWatchlistByUserId(Long userId) {
        return watchlistRepository.findByUserId(userId).stream()
            .map(watchlistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void removeVideoFromWatchlist(Long userId, Long videoId) {
        List<Watchlist> watchlistEntries = watchlistRepository.findByUserId(userId);
        watchlistEntries.stream()
            .filter(watchlist -> watchlist.getVideo().getId().equals(videoId))
            .findFirst()
            .ifPresent(watchlistRepository::delete);
    }
}
