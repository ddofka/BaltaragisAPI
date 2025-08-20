package org.codeacademy.baltaragisapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "artist_profile")
public class ArtistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "hero_image_url")
    private String heroImageUrl;

    @Column(columnDefinition = "TEXT")
    private String socials;

}


