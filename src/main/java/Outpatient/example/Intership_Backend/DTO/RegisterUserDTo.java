package Outpatient.example.Intership_Backend.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTo {

    //add email field
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(USER|DOCTOR)$", message = "Role can be USER or DOCTOR")
    private String role;

}
