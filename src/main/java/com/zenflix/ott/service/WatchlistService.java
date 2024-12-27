package com.zenflix.ott.service;

import java.util.List;

import com.zenflix.ott.dto.WatchlistDTO;

public interface WatchlistService {
    WatchlistDTO addVideoToWatchlist(WatchlistDTO watchlistDTO);
    List<WatchlistDTO> getWatchlistByUserId(Long userId);
    void removeVideoFromWatchlist(Long userId, Long videoId);
}
