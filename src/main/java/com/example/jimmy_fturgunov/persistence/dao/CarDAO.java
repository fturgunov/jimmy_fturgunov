package com.example.jimmy_fturgunov.persistence.dao;

import com.example.jimmy_fturgunov.persistence.dao.common.AbstractSimpleDAO;
import com.example.jimmy_fturgunov.persistence.model.Car;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CarDAO extends AbstractSimpleDAO<Car, Car, Long> {

    public Optional<Car> findByUniqueId(String uniqueId) {
        return Optional.ofNullable((Car) getSession()
                .createQuery("FROM Car c WHERE c.uniqueId = :uniqueId")
                .setParameter("uniqueId", uniqueId)
                .uniqueResult());
    }
}