package com.nequi.franchise.domain.model;

/**
 * Base interface for all domain model objects.
 * @param <ID> The type of the identifier for the model
 */
public interface Model<ID> {
    ID getId();
    void setId(ID id);
}
