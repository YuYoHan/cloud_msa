package com.example.catalogsservice.controller;

import com.example.catalogsservice.dto.ResponseCatalog;
import com.example.catalogsservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's working in User Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<?> getCatalogs() {
        List<ResponseCatalog> catalogList = catalogService.getAllCatalogs();
        return ResponseEntity.ok().body(catalogList);
    }
}
