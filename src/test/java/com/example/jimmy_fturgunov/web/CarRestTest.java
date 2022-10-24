package com.example.jimmy_fturgunov.web;

import com.example.jimmy_fturgunov.AbstractTest;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.service.CarService;
import com.example.jimmy_fturgunov.service.ReservationService;
import com.fasterxml.jackson.core.type.TypeReference;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CarRestTest extends AbstractTest {

    @Autowired
    private CarService carService;

    @Autowired
    private ReservationService reservationService;

    @Before
    public void setUp() {
        super.setUp();
        reservationService.deleteAllReservations();
        carService.deleteAllCars();
        carService.createCar(new Car("C" + RandomStringUtils.randomNumeric(3)));
        carService.createCar(new Car("C" + RandomStringUtils.randomNumeric(4)));
    }

    @Test
    public void listOfAllCarsTest() throws Exception {
        String uriListOfAllCars = "/api/public/car/list";

        MvcResult res = mvc.perform(
                MockMvcRequestBuilders
                        .get(uriListOfAllCars)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String content = res.getResponse().getContentAsString();
        List<Car> result = super.mapFromJson(content, new TypeReference<>() {
        });

        assertEquals(2, result.size());
    }
}
