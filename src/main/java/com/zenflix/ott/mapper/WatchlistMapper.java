package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.WatchlistDTO;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.Video;
import com.zenflix.ott.entity.Watchlist;
@Component
public class WatchlistMapper {
    public WatchlistDTO toDTO(Watchlist watchlist) {
        if (watchlist == null) {
            return null;
        }
        WatchlistDTO watchlistDTO = new WatchlistDTO();
        watchlistDTO.setId(watchlist.getId());
        watchlistDTO.setUserId(watchlist.getUser() != null ? watchlist.getUser().getId() : null);
        watchlistDTO.setVideoId(watchlist.getVideo() != null ? watchlist.getVideo().getId() : null);
        return watchlistDTO;
    }

    public Watchlist toEntity(WatchlistDTO watchlistDTO, User user, Video video) {
        if (watchlistDTO == null) {
            return null;
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setId(watchlistDTO.getId());
        watchlist.setUser(user);
        watchlist.setVideo(video);
        watchlist.setDeleted(false); // Default to false
        return watchlist;
    }
}
