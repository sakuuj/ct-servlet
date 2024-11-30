package by.sakujj.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ClientRequest(
        @Pattern(regexp = "\\S{1,30}")
        @NotBlank
        String username,

        @Email
        @NotBlank
        @Length(max = 40)
        String email,

        @NotBlank
        @Pattern(regexp = "\\S{1,60}")
        String notHashedPassword,

        @Min(14)
        @Max(120)
        @NotNull
        Integer age
) {
}
