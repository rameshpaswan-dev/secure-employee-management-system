package in.ramesh.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets and spaces")
    private String name;

    @NotBlank(message = "Department is required")
    @Size(min = 2, max = 50, message = "Department must be between 2 and 50 characters")
    private String department;

    @NotBlank(message = "Role is required")
    @Size(min = 2, max = 50, message = "Role must be between 2 and 50 characters")
    private String role;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than 0")
    @Max(value = 10000000, message = "Salary exceeds allowed limit")
    private Double salary;
}