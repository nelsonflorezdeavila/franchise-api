package com.nequi.franchise.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "branches")
public class Branch implements Model<String> {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String address;

    private String city;

    private String phone;

    private String email;

    @Indexed
    private String franchiseId;

    @Builder.Default
    private boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
