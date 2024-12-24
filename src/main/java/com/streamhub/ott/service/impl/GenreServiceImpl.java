package com.streamhub.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.streamhub.ott.dto.GenreDTO;
import com.streamhub.ott.entity.Genre;
import com.streamhub.ott.exception.ResourceNotFoundException;
import com.streamhub.ott.mapper.GenreMapper;
import com.streamhub.ott.repository.GenreRepository;
import com.streamhub.ott.service.GenreService;

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
