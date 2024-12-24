package com.streamhub.ott.mapper;

import org.springframework.stereotype.Component;

import com.streamhub.ott.dto.GenreDTO;
import com.streamhub.ott.entity.Genre;

@Component
public class GenreMapper {
    public GenreDTO toDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        genreDTO.setDeleted(genre.getDeleted());    
        return genreDTO;
    }

    public Genre toEntity(GenreDTO genreDTO) {
        if (genreDTO == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(genreDTO.getId());
        genre.setName(genreDTO.getName());
        genre.setDeleted(genreDTO.getDeleted() != null ? genreDTO.getDeleted() : false);// Default to false
        return genre;
    }
}