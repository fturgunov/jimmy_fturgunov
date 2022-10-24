package com.example.jimmy_fturgunov.service;

import com.example.jimmy_fturgunov.common.exceptions.NoAvailableCarsException;
import com.example.jimmy_fturgunov.common.exceptions.RequestedCarInUnavailableTimeFrameException;
import com.example.jimmy_fturgunov.common.requests.CreateReservationRequest;
import com.example.jimmy_fturgunov.persistence.dao.ReservationDAO;
import com.example.jimmy_fturgunov.persistence.dao.common.GenericDAO;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.persistence.model.Reservation;
import com.example.jimmy_fturgunov.service.common.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService extends AbstractService<Reservation, Reservation, Long> {

    private final ReservationDAO reservationDAO;

    @Override
    protected GenericDAO<Reservation, Reservation, Long> getServiceDAO() {
        return reservationDAO;
    }

    @Transactional
    public Reservation tryToCreateReservation(CreateReservationRequest createReservationRequest) {
        LocalDateTime startDateTime = createReservationRequest.getStartDateTime().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime endDateTime = startDateTime.plusHours(createReservationRequest.getDuration());
        verifyCreateReservationRequest(createReservationRequest, startDateTime, endDateTime);

        List<Car> availableCars = getAvailableCars(startDateTime, endDateTime);
        if (availableCars.isEmpty()) {
            throw new NoAvailableCarsException("Error: There is no available cars in time frame from " + startDateTime + " to "
                    + endDateTime);
        }

        Reservation reservation = new Reservation();
        reservation.setCar(availableCars.get(0));
        reservation.setStartDateTime(startDateTime);
        reservation.setEndDateTime(endDateTime);
        return reservationDAO.saveOrUpdate(reservation);
    }

    @Transactional
    public void deleteAllReservations() {
        reservationDAO.findAll().stream().forEach(r -> reservationDAO.delete(r));
    }

    private void verifyCreateReservationRequest(CreateReservationRequest createReservationRequest, LocalDateTime startDateTime
            , LocalDateTime endDateTime) {
        if (startDateTime.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))) {
            throw new RequestedCarInUnavailableTimeFrameException("Error: Requested car is before the current time." +
                    "The start time should be not less than " + LocalDateTime.now().truncatedTo(ChronoUnit.HOURS));
        }
        if (createReservationRequest.getDuration() < 1 || createReservationRequest.getDuration() > 2) {
            throw new RequestedCarInUnavailableTimeFrameException("Error: Duration can be from 1 up to 2 hours.");
        }
        if (endDateTime.isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(24))) {
            throw new RequestedCarInUnavailableTimeFrameException("Error: Requested car is after available 24 hours time frame. " +
                    "The end time should be not more than " + LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(24));
        }
    }

    private List<Car> getAvailableCars(LocalDateTime start, LocalDateTime end) {
        return reservationDAO.getAvailableCarsInTimeFrame(start, end);
    }
}