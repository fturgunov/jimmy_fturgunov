package com.example.jimmy_fturgunov.service;

import com.example.jimmy_fturgunov.AbstractTest;
import com.example.jimmy_fturgunov.common.exceptions.RequestedCarInUnavailableTimeFrameException;
import com.example.jimmy_fturgunov.common.requests.CreateReservationRequest;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.persistence.model.Reservation;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReservationTest extends AbstractTest {

    @Autowired
    private CarService carService;

    @Autowired
    private ReservationService reservationService;

    private LocalDateTime localDateTimeNowPlus10hour = LocalDateTime.now().plusHours(10);
    private Car car1;
    private Car car2;

    @Before
    public void setUp() {
        reservationService.deleteAllReservations();
        this.car1 = carService.createCar(new Car("C" + RandomStringUtils.randomNumeric(5)));
        this.car2 = carService.createCar(new Car("C" + RandomStringUtils.randomNumeric(6)));
    }

    @After
    public void cleanUp() {
        reservationService.deleteAllReservations();
        carService.delete(car1.getId());
        carService.delete(car2.getId());
    }

    @Test
    public void createReservationTest() {
        LocalDateTime localDateTimeNowPlus1hour = LocalDateTime.now().plusHours(1);
        CreateReservationRequest req = new CreateReservationRequest(localDateTimeNowPlus1hour, 1);
        Reservation reservation = reservationService.tryToCreateReservation(req);

        assertThat(reservation.getId(), is(notNullValue()));
        assertThat(reservation.getCar(), is(notNullValue()));
        assertThat(reservation.getStartDateTime(), is(equalTo(localDateTimeNowPlus1hour.truncatedTo(ChronoUnit.HOURS))));
        assertThat(reservation.getEndDateTime(), is(equalTo(localDateTimeNowPlus1hour.truncatedTo(ChronoUnit.HOURS).plusHours(1))));
    }

    @Test
    public void createReservationTest_WithAvailableReservations() {
        LocalDateTime localDateTimeNowPlus2hour = LocalDateTime.now().plusHours(2);

        // lets create reservations for one car for all the period of six hours
        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus2hour, 2));
        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus2hour.plusHours(2), 2));
        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus2hour.plusHours(4), 2));

        // and lets create reservations for second car for period from 0-3 and 5-6 hours.
        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus2hour, 2));
        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus2hour.plusHours(2), 1));
        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus2hour.plusHours(5), 1));

        // So second car should be available in frame 3-5 hours.
        CreateReservationRequest req = new CreateReservationRequest(localDateTimeNowPlus2hour.plusHours(3), 2);
        Reservation reservation = reservationService.tryToCreateReservation(req);

        assertThat(reservation.getId(), is(notNullValue()));
        assertThat(reservation.getCar(), is(notNullValue()));
        assertThat(reservation.getStartDateTime(), is(equalTo(localDateTimeNowPlus2hour.truncatedTo(ChronoUnit.HOURS).plusHours(3))));
        assertThat(reservation.getEndDateTime(), is(equalTo(localDateTimeNowPlus2hour.truncatedTo(ChronoUnit.HOURS).plusHours(5))));
    }


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void createReservationTest_BeforeCurrentTime_ThrowsException() {
        exceptionRule.expect(RequestedCarInUnavailableTimeFrameException.class);

        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus10hour.plusHours(-11), 1));
    }

    @Test
    public void createReservationTest_After24Hours_ThrowsException() {
        exceptionRule.expect(RequestedCarInUnavailableTimeFrameException.class);

        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus10hour.plusHours(13), 2));
    }

    @Test
    public void createReservationTest_DurationTooSmall_ThrowsException() {
        exceptionRule.expect(RequestedCarInUnavailableTimeFrameException.class);

        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus10hour, -1));
    }

    @Test
    public void createReservationTest_DurationTooBig_ThrowsException() {
        exceptionRule.expect(RequestedCarInUnavailableTimeFrameException.class);

        reservationService.tryToCreateReservation(new CreateReservationRequest(localDateTimeNowPlus10hour, 3));
    }

}
