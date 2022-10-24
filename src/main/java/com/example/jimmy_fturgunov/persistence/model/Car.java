package com.example.jimmy_fturgunov.persistence.model;

import com.example.jimmy_fturgunov.persistence.model.common.IEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "car")
public class Car implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Size(min = 1, max = 30, message = "The field 'make' should not be empty or exceed 30 symbols in length")
    @Column(name = "make")
    private String make;

    @Size(min = 1, max = 30, message = "The field 'model' should not be empty or exceed 30 symbols in length")
    @Column(name = "model")
    private String model;

    @Size(min = 1, max = 10, message = "The field 'unique_id' should not be empty or exceed 10 symbols in length")
    @Column(name = "unique_id", unique = true)
    private String uniqueId;

    public Car(@Size(max = 10) String uniqueId) {
        this.uniqueId = uniqueId;
        this.make = "default";
        this.model = "default";
    }
}
