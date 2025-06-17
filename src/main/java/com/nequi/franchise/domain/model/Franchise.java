package com.nequi.franchise.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franchises")
public class Franchise implements Model<String> {

    @Id
    private String id;
    private String name;
    private String description;
    private boolean active;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Franchise withId(String id) {
        return Franchise.builder()
            .id(id)
            .name(this.name)
            .description(this.description)
            .active(this.active)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }
}
