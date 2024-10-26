package by.sakujj.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ClientResponse(
        UUID id,
        String username,
        String email,
        Integer age
) {
}
