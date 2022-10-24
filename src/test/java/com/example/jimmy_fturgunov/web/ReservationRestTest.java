package com.example.jimmy_fturgunov.web;

import com.example.jimmy_fturgunov.AbstractTest;
import com.example.jimmy_fturgunov.common.requests.CreateReservationRequest;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.persistence.model.Reservation;
import com.example.jimmy_fturgunov.service.CarService;
import com.example.jimmy_fturgunov.service.ReservationService;
import com.fasterxml.jackson.core.type.TypeReference;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationRestTest extends AbstractTest {

    @Autowired
    private CarService carService;

    @Before
    public void setUp() {
        super.setUp();
        carService.createCar(new Car("C" + RandomStringUtils.randomNumeric(3)));
    }

    @Test
    public void tryToCreateReservationTest() throws Exception {
        String rootServiceURI = "/api/public/reservation";
        LocalDateTime localDateTimeNowPlus1hour = LocalDateTime.now().plusHours(1);

        CreateReservationRequest req = new CreateReservationRequest(localDateTimeNowPlus1hour, 1);

        Long idOfCreatedReservation;

        {// create
            MvcResult res = mvc.perform(
                    post(rootServiceURI + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(super.mapToJson(req))
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andReturn();
            String content = res.getResponse().getContentAsString();
            Reservation responce = super.mapFromJson(content, Reservation.class);

            assertNotNull(responce.getId());
            assertNotNull(responce.getCar().getId());
            assertEquals(responce.getStartDateTime(), localDateTimeNowPlus1hour.truncatedTo(ChronoUnit.HOURS));
            assertEquals(responce.getEndDateTime(), localDateTimeNowPlus1hour.plusHours(1).truncatedTo(ChronoUnit.HOURS));

            idOfCreatedReservation = responce.getId();
        }

        {// get all
            MvcResult res = mvc.perform(get(rootServiceURI + "/list")).andReturn();
            String content = res.getResponse().getContentAsString();
            List<Reservation> responce = super.mapFromJson(content, new TypeReference<>() {
            });

            assertTrue(responce.size() > 0);
            assertTrue(responce.stream().filter(r -> r.getId() == idOfCreatedReservation).findFirst().isPresent());
        }
    }

}
