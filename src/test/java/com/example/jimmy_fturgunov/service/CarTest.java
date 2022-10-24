package com.example.jimmy_fturgunov.service;

import com.example.jimmy_fturgunov.AbstractTest;
import com.example.jimmy_fturgunov.common.exceptions.UniqueIdFormatViolationException;
import com.example.jimmy_fturgunov.persistence.model.Car;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CarTest extends AbstractTest {

    @Autowired
    private CarService carService;

    @Autowired
    private ReservationService reservationService;

    @After
    public void cleanUp() {
        reservationService.deleteAllReservations();
        carService.deleteAllCars();
    }

    @Test
    public void createCar() {
        Car car = new Car("C" + RandomStringUtils.randomNumeric(9));
        Car saved = carService.createCar(car);

        assertThat(saved, is(notNullValue()));
        assertThat(saved.getId(), is(notNullValue()));

        Car found = carService.findById(saved.getId());
        assertThat(found.getUniqueId(), is(equalTo(car.getUniqueId())));
    }

    @Test
    public void updateCar() {
        Car car = new Car("C" + RandomStringUtils.randomNumeric(8));
        Car saved = carService.createCar(car);
        assertThat(saved.getUniqueId(), is(equalTo(car.getUniqueId())));
        assertThat(saved.getMake(), is(equalTo("default")));
        assertThat(saved.getModel(), is(equalTo("default")));

        Car updatedCar = new Car("C" + RandomStringUtils.randomNumeric(9));
        updatedCar.setMake("updated");
        updatedCar.setModel("updated");
        updatedCar.setId(saved.getId());

        carService.updateCar(updatedCar);

        Car found = carService.findById(saved.getId());
        assertThat(found.getUniqueId(), is(equalTo(updatedCar.getUniqueId())));
        assertThat(found.getMake(), is(equalTo(updatedCar.getMake())));
        assertThat(found.getModel(), is(equalTo(updatedCar.getModel())));
    }

    @Test
    public void deleteCar() {
        Car car = new Car("C" + RandomStringUtils.randomNumeric(9));
        Car saved = carService.createCar(car);

        carService.deleteCarById(saved.getId());
        Car found = carService.findById(saved.getId());
        assertThat(found, is(nullValue()));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void createCarTest_UniqueIdTooShort_throwsException() {
        exceptionRule.expect(UniqueIdFormatViolationException.class);

        Car car = new Car("C");
        carService.createCar(car);
    }

    @Test
    public void createCarTest_UniqueIdTooLong_throwsException() {
        exceptionRule.expect(UniqueIdFormatViolationException.class);

        Car car = new Car("C1234567890");
        carService.createCar(car);
    }

    @Test
    public void createCarTest_UniqueIdIsNotFromLetterC_throwsException() {
        exceptionRule.expect(UniqueIdFormatViolationException.class);

        Car car = new Car("D01");
        carService.createCar(car);
    }

    @Test
    public void createCarTest_UniqueIdSecondPartIsNotNumerical_throwsException() {
        exceptionRule.expect(UniqueIdFormatViolationException.class);

        Car car = new Car("C01a");
        carService.createCar(car);
    }

    @Test
    public void updateCarTest_UniqueIdTooShort_throwsException() {
        exceptionRule.expect(UniqueIdFormatViolationException.class);

        Car car = new Car("C");
        carService.updateCar(car);
    }
}
