package com.nutech.tes.services;

import com.nutech.tes.ApiRespons;
import com.nutech.tes.banner.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SerService {

    private final JdbcTemplate jdbcTemplate;

    // Mendapatkan semua data service
    public ResponseEntity<Object> findAll(){
        List<Services> services =new ArrayList<>();

        String sql="SELECT * FROM services"; // Query untuk mendapatkan semua data service

        // Mendapatkan semua data service
        jdbcTemplate.query(sql,(rs,rowNum)->{
            Services service= new Services();
            service.setId(rs.getInt("id"));
            service.setService_code(rs.getString("service_code"));
            service.setService_name(rs.getString("service_name"));
            service.setService_icon(rs.getString("service_icon"));
            service.setService_tarif(rs.getInt("service_tarif"));

            return services.add(service);
        });

        // Memanggil class ApiRespons untuk dijadikan sebagai response kepada user
        ApiRespons<List<Services>> apiRespons= new ApiRespons<>();

        if (services.size() == 0) {
            // Handle exception jika data service tidak ditemukan
            apiRespons.setStatus(404);
            apiRespons.setMessage("Data service tidak ditemukan");
            apiRespons.setData(null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiRespons);
        }

        apiRespons.setStatus(200);
        apiRespons.setMessage("sukses");
        apiRespons.setData(services);
        return ResponseEntity.status(HttpStatus.OK).body(apiRespons);
    }
}
