package com.example.catalogsservice.service;

import com.example.catalogsservice.dto.ResponseCatalog;

import java.util.List;

public interface CatalogService {
    List<ResponseCatalog> getAllCatalogs();
}
