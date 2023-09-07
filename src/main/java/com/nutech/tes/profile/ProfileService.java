package com.nutech.tes.profile;

import com.nutech.tes.ApiRespons;
import com.nutech.tes.security.jwt.JwtUtils;
import com.nutech.tes.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProfileService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(DataSource dataSource, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    // Registrasi user
    public ResponseEntity<Object> registration(RegistrationRequest request) {
        Profile profile = new Profile();
        profile.setEmail(request.getEmail());
        profile.setPassword(passwordEncoder.encode(request.getPassword()));
        profile.setFirst_name(request.getFirst_name());
        profile.setLast_name(request.getLast_name());
        profile.setProfile_image(request.getProfile_image());

        ApiRespons<String> apiRespons = new ApiRespons<>();

        if (isEmailExists(profile.getEmail())) {
            apiRespons.setStatus(400);
            apiRespons.setMessage("Email sudah terdaftar");
            apiRespons.setData(null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiRespons);
        }

        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            String sql = "INSERT INTO profile (email, password, first_name, last_name, profile_image) VALUES (?, ?, " +
                    "?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, profile.getEmail());
                preparedStatement.setString(2, profile.getPassword());
                preparedStatement.setString(3, profile.getFirst_name());
                preparedStatement.setString(4, profile.getLast_name());
                preparedStatement.setString(5, profile.getProfile_image());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            apiRespons.setStatus(400);
            apiRespons.setMessage("Gagal menyimpan data");
            apiRespons.setData(null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiRespons);
        }

        apiRespons.setStatus(200);
        apiRespons.setMessage("Registration berhasil, silahkan login");
        apiRespons.setData(null);

        return ResponseEntity.status(HttpStatus.OK).body(apiRespons);
    }

    // Login user
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        // Authenticate user with loginRequest
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Set authenticate
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT Token from local authentication user
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get user
        UserDetailsImpl users = (UserDetailsImpl) authentication.getPrincipal();

        List<Object> response = new ArrayList<>();

        response.add(users);
        response.add(jwt);

        // Return user detail response
        return ResponseEntity.ok().body(response);
    }

    // Cek jika email user sudah terdaftar ke database
    private boolean isEmailExists(String email) {
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            String sql = "SELECT COUNT(*) FROM profile WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
