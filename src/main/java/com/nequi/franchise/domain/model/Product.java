package com.nequi.franchise.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product implements Model<String> {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private BigDecimal price;

    private String category;

    @Builder.Default
    private boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
