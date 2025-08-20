package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class ArtistDto {
    @Schema(example = "Baltaragis") String name;
    @Schema(example = "Contemporary artist blending traditional forms.") String bio;
    @Schema(example = "https://cdn.example.com/hero.jpg") String heroImageUrl;
    @Schema(example = "{\"instagram\":\"https://instagram.com/baltaragis\"}") String socials;
}


