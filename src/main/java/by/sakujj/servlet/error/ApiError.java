package by.sakujj.servlet.error;

import lombok.Builder;

@Builder
public record ApiError(String message) {
}
