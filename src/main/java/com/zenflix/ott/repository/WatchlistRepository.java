package com.zenflix.ott.repository;

import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.Video;
import com.zenflix.ott.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserId(Long userId);
    List<Watchlist> findByVideoId(Long videoId);
    List<Watchlist> findAllByDeletedFalse();
    boolean existsByUserAndVideoAndDeletedFalse(User user, Video video);
    List<Watchlist> findByUserIdAndDeletedFalse(Long userId);
}