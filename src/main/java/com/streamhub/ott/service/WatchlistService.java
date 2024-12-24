package com.streamhub.ott.service;

import java.util.List;

import com.streamhub.ott.dto.WatchlistDTO;

public interface WatchlistService {
    WatchlistDTO addVideoToWatchlist(WatchlistDTO watchlistDTO);
    List<WatchlistDTO> getWatchlistByUserId(Long userId);
    void removeVideoFromWatchlist(Long userId, Long videoId);
}
