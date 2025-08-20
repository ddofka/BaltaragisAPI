package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.dto.ArtistDto;
import org.codeacademy.baltaragisapi.mapper.ArtistMapper;
import org.codeacademy.baltaragisapi.repository.ArtistProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {

    private final ArtistProfileRepository artistProfileRepository;
    private final ArtistMapper artistMapper;

    public ArtistService(ArtistProfileRepository artistProfileRepository, ArtistMapper artistMapper) {
        this.artistProfileRepository = artistProfileRepository;
        this.artistMapper = artistMapper;
    }

    public ArtistDto getProfile() {
        return artistProfileRepository.fetchSingleProfile()
                .map(artistMapper::toDto)
                .orElse(null);
    }
}


