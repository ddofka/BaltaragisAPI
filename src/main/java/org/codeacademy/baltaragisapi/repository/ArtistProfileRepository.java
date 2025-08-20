package org.codeacademy.baltaragisapi.repository;

import org.codeacademy.baltaragisapi.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
    default Optional<ArtistProfile> fetchSingleProfile() {
        return findAll().stream().findFirst();
    }
}


