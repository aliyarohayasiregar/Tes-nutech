package com.nutech.tes.banner;

import com.nutech.tes.ApiRespons;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final JdbcTemplate jdbcTemplate;

    // Method untuk mendapatkan semua data banner
    public ResponseEntity<Object> findAll() {
        List<Banner> banners = new ArrayList<>();

        String sql = "SELECT * FROM banner"; // Query untuk mendapatkan semua data Banner

        // Mendapatkan semua data banner
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            Banner banner = new Banner();
            banner.setId(rs.getInt("id"));
            banner.setBanner_name(rs.getString("banner_name"));
            banner.setBanner_image(rs.getString("banner_image"));
            banner.setDescription(rs.getString("description"));

            return banners.add(banner);
        });

        // Memanggil class ApiRespons untuk dijadikan sebagai response kepada user
        ApiRespons<List<Banner>> apiRespons = new ApiRespons<>();
        System.out.println(banners.size());

        if (banners.size() == 0) {
            // Handle exception jika data banner tidak ditemukan
            apiRespons.setStatus(404);
            apiRespons.setMessage("Data banner tidak ditemukan");
            apiRespons.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiRespons);
        }

        apiRespons.setStatus(200);
        apiRespons.setMessage("sukses");
        apiRespons.setData(banners);

        return ResponseEntity.status(HttpStatus.OK).body(apiRespons);
    }

}
