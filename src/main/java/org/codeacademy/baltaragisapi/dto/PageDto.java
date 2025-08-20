package org.codeacademy.baltaragisapi.dto;

import java.time.OffsetDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class PageDto {
    @Schema(example = "Home") String title;
    @Schema(example = "home") String slug;
    @Schema(example = "# Welcome to Baltaragis") String contentMd;
    @Schema(description = "Last update timestamp") OffsetDateTime updatedAt;
}


