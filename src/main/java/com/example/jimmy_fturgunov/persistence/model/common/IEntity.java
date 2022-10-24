package com.example.jimmy_fturgunov.persistence.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface IEntity<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);

    @JsonIgnore
    default boolean isNew() {
        return getId() == null;
    }
}
