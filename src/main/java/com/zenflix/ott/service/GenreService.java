package com.zenflix.ott.service;

import java.util.List;

import com.zenflix.ott.dto.GenreDTO;

public interface GenreService {
    GenreDTO createGenre(GenreDTO genreDTO);
    GenreDTO getGenreById(Long id);
    List<GenreDTO> getAllGenres();
    GenreDTO updateGenre(Long id, GenreDTO genreDTO);
    void deleteGenre(Long id);
}
