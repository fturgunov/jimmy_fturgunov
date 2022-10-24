package com.example.jimmy_fturgunov.persistence.dao.common;

import com.example.jimmy_fturgunov.common.exceptions.DAOException;
import com.example.jimmy_fturgunov.persistence.model.common.IEntity;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<VIEWENTITY extends IEntity, CRUDENTITY extends IEntity, ID extends Serializable> {

    /**
     * Method returns all entities
     *
     * @return list of the entities
     */
    List<CRUDENTITY> findAll() throws DAOException;

    VIEWENTITY findViewById(ID id);

    /**
     * Method returns the entity by specified identifier.
     *
     * @param id identifier
     * @return entity
     */
    CRUDENTITY findById(ID id) throws DAOException;

    /**
     * Method saves or updates the entity
     *
     * @param entity entity
     */
    CRUDENTITY saveOrUpdate(CRUDENTITY entity) throws DAOException;

    CRUDENTITY insert(CRUDENTITY entity);

    void update(CRUDENTITY entity) throws DAOException;

    /**
     * Method deletes entity by specified identifier
     *
     * @param entity entity for removing. Identifier field and not-null fields should be populated.
     */
    void delete(CRUDENTITY entity) throws DAOException;
}
