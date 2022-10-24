package com.example.jimmy_fturgunov.common.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {

    @NotNull(message = "startDateTime should be in format 2022-10-18T23:00:00 (will be rounded down to whole hours)")
    private LocalDateTime startDateTime;

    @Min(value = 1, message = "Duration can be from 1 up to 2 hours.")
    @Max(value = 2, message = "Duration can be from 1 up to 2 hours.")
    private Integer duration;
}
