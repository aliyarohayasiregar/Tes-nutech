package com.nutech.tes.profile;

import com.nutech.tes.ApiRespons;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("registration")
    public ResponseEntity<Object> registration(@Valid @RequestBody RegistrationRequest request) {
        return profileService.registration(request);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return profileService.login(request);
    }

    // Handle exception of error validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.add(fieldError.getDefaultMessage());
        });
        ApiRespons<Object> apiRespons = new ApiRespons<>();
        apiRespons.setStatus(400);
        apiRespons.setMessage("Inputan tidak sesuai");
        apiRespons.setData(errors);
        return ResponseEntity.badRequest().body(apiRespons);
    }

}
