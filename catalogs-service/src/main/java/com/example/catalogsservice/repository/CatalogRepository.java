package com.example.catalogsservice.repository;

import com.example.catalogsservice.entity.CalalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends JpaRepository<CalalogEntity, Long> {
    CalalogEntity findByProductId(String productId);
}
