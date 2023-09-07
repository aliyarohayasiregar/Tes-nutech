package com.nutech.tes.security.service;

import com.nutech.tes.profile.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DataSource dataSource;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profile profile = new Profile();

        String sql = "SELECT * FROM profile WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    profile.setId(resultSet.getInt("id"));
                    profile.setEmail(resultSet.getString("email"));
                    profile.setPassword(resultSet.getString("password"));
                    profile.setFirst_name(resultSet.getString("first_name"));
                    profile.setLast_name(resultSet.getString("last_name"));
                    profile.setProfile_image(resultSet.getString("profile_image"));
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return UserDetailsImpl.build(profile);
    }

}
