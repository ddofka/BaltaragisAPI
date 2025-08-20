package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.dto.admin.UpdateArtistProfileRequest;
import org.codeacademy.baltaragisapi.entity.ArtistProfile;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.repository.ArtistProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@Transactional
public class AdminArtistService {

    private final ArtistProfileRepository artistRepository;

    public AdminArtistService(ArtistProfileRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public ArtistProfile getArtistProfile() {
        return artistRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Artist profile not found"));
    }

    public ArtistProfile updateArtistProfile(UpdateArtistProfileRequest request) {
        ArtistProfile profile = getArtistProfile();
        
        if (request.getName() != null) {
            profile.setName(request.getName());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getHeroImageUrl() != null) {
            profile.setHeroImageUrl(request.getHeroImageUrl());
        }
        if (request.getSocials() != null) {
            profile.setSocials(request.getSocials());
        }
        
        profile.setUpdatedAt(OffsetDateTime.now());
        return artistRepository.save(profile);
    }
}
