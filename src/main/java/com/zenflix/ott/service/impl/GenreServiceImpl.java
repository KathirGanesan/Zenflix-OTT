package com.zenflix.ott.service.impl;

import com.zenflix.ott.dto.GenreDTO;
import com.zenflix.ott.entity.Genre;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.GenreMapper;
import com.zenflix.ott.repository.GenreRepository;
import com.zenflix.ott.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public GenreDTO createGenre(GenreDTO genreDTO) {
        // Check if a genre with the same name already exists
        if (genreRepository.existsByName(genreDTO.getName())) {
            throw new IllegalArgumentException("A genre with this name already exists.");
        }

        Genre genre = genreMapper.toEntity(genreDTO);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDTO(savedGenre);
    }


    @Override
    public GenreDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        return genreMapper.toDTO(genre);
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAllByDeletedFalse().stream()
            .map(genreMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public GenreDTO updateGenre(Long id, GenreDTO genreDTO) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        // Check if another genre with the same name exists, excluding the current one
        if (genreRepository.existsByName(genreDTO.getName()) &&
                !existingGenre.getName().equals(genreDTO.getName())) {
            throw new IllegalArgumentException("A genre with this name already exists.");
        }

        existingGenre.setName(genreDTO.getName());
        Genre updatedGenre = genreRepository.save(existingGenre);
        return genreMapper.toDTO(updatedGenre);
    }


    @Override
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        genre.setDeleted(true);
        genreRepository.save(genre);
    }
}
