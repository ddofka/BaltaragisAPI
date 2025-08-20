package org.codeacademy.baltaragisapi.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    String code;
    String message;
}


