package org.codeacademy.baltaragisapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class WaitlistRequest {
    @Schema(example = "user@example.com") String email;
}


