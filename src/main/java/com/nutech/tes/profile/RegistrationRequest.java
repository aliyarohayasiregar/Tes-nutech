package com.nutech.tes.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Email tidak valid")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 5, max = 20, message = "Panjang password harus antara 5 dan 20 karakter")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]*$", message = "Password tidak valid")
    private String password;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 3, max = 25, message = "Nama harus memiliki panjang antara 3 dan 25 karakter")
    @Pattern(regexp = "^[a-zA-Z'-]+( [a-zA-Z'-]+)*[a-zA-Z'-]$", message = "Format nama tidak valid")
    private String first_name;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 3, max = 25, message = "Nama harus memiliki panjang antara 3 dan 25 karakter")
    @Pattern(regexp = "^[a-zA-Z'-]+( [a-zA-Z'-]+)*[a-zA-Z'-]$", message = "Format nama tidak valid")
    private String last_name;

    private String profile_image;

}
