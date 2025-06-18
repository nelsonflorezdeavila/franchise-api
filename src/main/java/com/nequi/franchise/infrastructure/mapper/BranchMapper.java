package com.nequi.franchise.infrastructure.mapper;

import com.nequi.franchise.application.dto.BranchRequest;
import com.nequi.franchise.application.dto.BranchResponse;
import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface BranchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "franchiseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Branch toEntity(BranchRequest request);

    @Mapping(target = "id", source = "branch.id")
    @Mapping(target = "name", source = "branch.name")
    @Mapping(target = "address", source = "branch.address")
    @Mapping(target = "city", source = "branch.city")
    @Mapping(target = "phone", source = "branch.phone")
    @Mapping(target = "email", source = "branch.email")
    @Mapping(target = "franchiseId", source = "branch.franchiseId")
    @Mapping(target = "franchiseName", source = "franchise.name")
    @Mapping(target = "active", source = "branch.active")
    @Mapping(target = "createdAt", source = "branch.createdAt")
    @Mapping(target = "updatedAt", source = "branch.updatedAt")
    BranchResponse toResponse(Branch branch, Franchise franchise);

    @Mapping(target = "franchiseName", ignore = true)
    BranchResponse toResponse(Branch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "franchiseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(BranchRequest request, @MappingTarget Branch branch);
}
