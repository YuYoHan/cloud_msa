package com.example.catalogsservice.service;


import com.example.catalogsservice.dto.ResponseCatalog;
import com.example.catalogsservice.entity.CalalogEntity;
import com.example.catalogsservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatalogServiceImpl implements CatalogService{
    private final CatalogRepository catalogRepository;

    @Override
    public List<ResponseCatalog> getAllCatalogs() {
        List<CalalogEntity> catalogs = catalogRepository.findAll();
        List<ResponseCatalog> response = catalogs.stream()
                .map(catalog -> new ModelMapper().map(catalog, ResponseCatalog.class))
                .collect(Collectors.toList());
        log.debug("catalogs {}", response);

        return response;
    }
}
