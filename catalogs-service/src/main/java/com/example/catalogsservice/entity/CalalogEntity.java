package com.example.catalogsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "catalog")
public class CalalogEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer unitPrice;
    // insertable = false: JPA가 이 컬럼의 값을 삽입할 때도 관여하지 않도록 합니다.
    // 즉, 엔티티 저장 시 이 필드의 값은 DB에 맡깁니다.
    @Column(nullable = false, updatable = false, insertable = false)
    // 티티를 저장할 때 JPA는 이 필드에 값을 넣지 않고, DB에서 자동으로 현재 시간이 들어가게 합니다.
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
