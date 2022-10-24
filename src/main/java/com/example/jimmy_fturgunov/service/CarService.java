package com.example.jimmy_fturgunov.service;

import com.example.jimmy_fturgunov.common.exceptions.ObjectDoesNotExistsException;
import com.example.jimmy_fturgunov.common.exceptions.UniqueIdFormatViolationException;
import com.example.jimmy_fturgunov.common.exceptions.ValueNotUniqueException;
import com.example.jimmy_fturgunov.persistence.dao.CarDAO;
import com.example.jimmy_fturgunov.persistence.dao.common.GenericDAO;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.service.common.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class CarService extends AbstractService<Car, Car, Long> {

    private Pattern patternIsNumber = Pattern.compile("-?\\d+(\\.\\d+)?");

    private final CarDAO carDAO;

    @Override
    protected GenericDAO<Car, Car, Long> getServiceDAO() {
        return carDAO;
    }

    @Transactional
    public Car createCar(Car car) {
        verifyUniqueIdFormat(car.getUniqueId());
        if (!carDAO.findByUniqueId(car.getUniqueId()).isEmpty()) {
            throw new ValueNotUniqueException("Error: car with unique_id '" + car.getUniqueId() + "' already exists.");
        };
        return carDAO.saveOrUpdate(car);
    }

    @Transactional
    public Car updateCar(Car carUpdated) {
        verifyUniqueIdFormat(carUpdated.getUniqueId());
        Long carId = carUpdated.getId();
        Car car = carDAO.findById(carId);
        if (car == null) {
            throw new ObjectDoesNotExistsException("Error: The car with id '" + carUpdated.getId() + "' does not exists.");
        }
        if (!carUpdated.getUniqueId().equals(car.getUniqueId())) {
            Optional<Car> carWithSameUniqueIdOptional = carDAO.findByUniqueId(carUpdated.getUniqueId());
            if (!carWithSameUniqueIdOptional.isEmpty() && carWithSameUniqueIdOptional.get().getId() != carId) {
                throw new ValueNotUniqueException("Error: car with unique_id '" + carUpdated.getUniqueId() + "' already exists.");
            };
        }

        car.setMake(carUpdated.getMake());
        car.setModel(carUpdated.getModel());
        car.setUniqueId(carUpdated.getUniqueId());
        return carDAO.saveOrUpdate(car);
    }

    @Transactional
    public void deleteAllCars() {
        carDAO.findAll().stream().forEach(c -> carDAO.delete(c));
    }

    @Transactional
    public void deleteCarById(Long id) {
        try {
            delete(id);
        } catch (Exception e) {
            throw new ObjectDoesNotExistsException("Error: The car with id '" + id + "' does not exists.");
        }
    }

    private void verifyUniqueIdFormat(String unique_id) {
        String errMsg = "Error: Please use the format 'C<number>' for car's unique_id (example 'C01'). ";
        if (unique_id.length() <= 1) {
            throw new UniqueIdFormatViolationException(errMsg + "The unique_id '" + unique_id + "' is too short.");
        } else if (unique_id.length() > 10) {
            throw new UniqueIdFormatViolationException(errMsg + "The unique_id '" + unique_id + "' is too long.");
        } else if (unique_id.charAt(0) != 'C') {
            throw new UniqueIdFormatViolationException(errMsg + "The unique_id '" + unique_id + "' have to start from 'C' letter.");
        } else {
            String expectedNumber = unique_id.substring(1);
            if (!patternIsNumber.matcher(expectedNumber).matches()) {
                throw new UniqueIdFormatViolationException(errMsg + "The part after 'C' letter of unique_id '" + unique_id + "' have to be a number.");
            }
        }
    }
}