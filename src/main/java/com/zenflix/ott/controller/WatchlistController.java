package com.zenflix.ott.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenflix.ott.dto.WatchlistDTO;
import com.zenflix.ott.security.RequiresSubscription;
import com.zenflix.ott.service.WatchlistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistController {
    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @RequiresSubscription
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id") // Only admins or the user themselves
    public ResponseEntity<WatchlistDTO> addVideoToWatchlist(@Valid @RequestBody WatchlistDTO watchlistDTO) {
        WatchlistDTO addedWatchlist = watchlistService.addVideoToWatchlist(watchlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addedWatchlist);
    }

    @RequiresSubscription
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id") // Only admins or the user themselves
    public ResponseEntity<List<WatchlistDTO>> getWatchlistByUserId(@PathVariable Long userId) {
        List<WatchlistDTO> userWatchlist = watchlistService.getWatchlistByUserId(userId);
        return ResponseEntity.ok(userWatchlist);
    }

    @RequiresSubscription
    @DeleteMapping("/user/{userId}/video/{videoId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id") // Only admins or the user themselves
    public ResponseEntity<Void> removeVideoFromWatchlist(
            @PathVariable Long userId,
            @PathVariable Long videoId
    ) {
        watchlistService.removeVideoFromWatchlist(userId, videoId);
        return ResponseEntity.noContent()
                .build();
    }
}
