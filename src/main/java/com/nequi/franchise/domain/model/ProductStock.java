package com.nequi.franchise.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_stocks")
@CompoundIndex(name = "product_branch_idx", def = "{'productId': 1, 'branchId': 1}", unique = true)
public class ProductStock implements Model<String> {

    @Id
    private String id;

    @Indexed
    private String productId;

    @Indexed
    private String branchId;

    @Builder.Default
    private Integer stock = 0;

    @Builder.Default
    private Integer minStock = 0;

    @Builder.Default
    private Integer maxStock = 1000;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
