package com.example.jimmy_fturgunov.persistence.dao;

import com.example.jimmy_fturgunov.persistence.dao.common.AbstractSimpleDAO;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.persistence.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationDAO extends AbstractSimpleDAO<Reservation, Reservation, Long> {

    public List<Car> getAvailableCarsInTimeFrame(LocalDateTime frameStart, LocalDateTime frameEnd) {
        return getSession()
                .createNativeQuery(
                        "SELECT " +
                            "    c.id, " +
                            "    c.make, " +
                            "    c.model, " +
                            "    c.unique_id " +
                            "FROM car c left join " +
                            "     (SELECT " +
                            "          r.id, " +
                            "          r.car_id " +
                            "      FROM reservation r " +
                            "      WHERE " +
                            "          (:frameStart <= r.start_date_time AND r.start_date_time < :frameEnd) OR " +
                            "          (:frameStart < r.end_date_time AND r.end_date_time <= :frameEnd) OR " +
                            "          (r.start_date_time <= :frameStart AND :frameEnd <= r.end_date_time) " +
                            "     ) as r2 " +
                            "     on c.id = r2.car_id " +
                            "where " +
                            "r2.car_id IS NULL"
                )
                .addEntity(Car.class)
                .setParameter("frameStart", frameStart)
                .setParameter("frameEnd", frameEnd)
                .list();
    }

}