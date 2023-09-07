package com.nutech.tes.services;

import com.nutech.tes.ApiRespons;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service")
public class ServiceController {

    private final SerService service;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return service.findAll();
    }

}
