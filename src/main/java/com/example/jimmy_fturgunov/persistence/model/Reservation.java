package com.example.jimmy_fturgunov.persistence.model;

import com.example.jimmy_fturgunov.persistence.model.common.IEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "reservation")
public class Reservation implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    public Reservation(Car car, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.car = car;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}