package com.example.jimmy_fturgunov.web;

import com.example.jimmy_fturgunov.common.requests.CreateReservationRequest;
import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.persistence.model.Reservation;
import com.example.jimmy_fturgunov.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/reservation")
public class ReservationRest {

    private final ReservationService reservationService;

    @Operation(summary = "Create a new reservation (success if there is an available car).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created a reservation",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class))}),
            @ApiResponse(responseCode = "417 (v1)", description = "Error: Requested car is before the current time." +
                    "The start time should be not less than 2022-10-24T16:00",
                    content = @Content),
            @ApiResponse(responseCode = "417 (v2)", description = "Error: Requested car is after available 24 hours time frame. " +
                    "The end time should be not more than than 2022-10-24T16:00",
                    content = @Content),
            @ApiResponse(responseCode = "417 (v3)", description = "Error: Duration can be from 1 up to 2 hours.",
                    content = @Content),
            @ApiResponse(responseCode = "417 (v4)", description = "Error: There is no available cars in time frame from" +
                    " 2022-10-24T16:00 to 2022-10-24T17:00",
                    content = @Content)
    })
    @PostMapping("/create")
    public Reservation tryToCreateReservation(@Valid @RequestBody CreateReservationRequest createReservationRequest) {
        return reservationService.tryToCreateReservation(createReservationRequest);
    }


    @Operation(summary = "Get the list off all reservations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Reservation.class)))})
    })
    @GetMapping("/list")
    public List<Reservation> listOfAllReservations() { return reservationService.findAll(); }
}