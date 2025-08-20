package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class PageDto {
    @Schema(example = "Home") String title;
    @Schema(example = "home") String slug;
    @Schema(example = "# Welcome to Baltaragis") String contentMd;
}


