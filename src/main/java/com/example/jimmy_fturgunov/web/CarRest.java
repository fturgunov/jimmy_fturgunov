package com.example.jimmy_fturgunov.web;

import com.example.jimmy_fturgunov.persistence.model.Car;
import com.example.jimmy_fturgunov.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/car")
public class CarRest {

    private final CarService carService;

    @Operation(summary = "Create a new car (based on info provided in the body of the request)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created a car",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "406 (v1)", description = "Error: car with unique_id 'CXXX' already exists.",
            content = @Content),
            @ApiResponse(responseCode = "406 (v2)", description = "Error: Please use the format 'C<number>' for car's unique_id " +
                    "(example 'C01'). The unique_id 'CXXX' is too short.",
                    content = @Content),
            @ApiResponse(responseCode = "406 (v3)", description = "Error: Please use the format 'C<number>' for car's unique_id " +
                    "(example 'C01'). The unique_id 'CXXX' is too long.",
                    content = @Content),
            @ApiResponse(responseCode = "406 (v4)", description = "Error: Please use the format 'C<number>' for car's unique_id " +
                    "(example 'C01'). The unique_id 'CXXX' have to start from 'C' letter.",
                    content = @Content),
            @ApiResponse(responseCode = "406 (v5)", description = "Error: Please use the format 'C<number>' for car's unique_id " +
                    "(example 'C01'). The part after 'C' letter of unique_id 'CXXX' have to be a number.",
                    content = @Content)
    })
    @PostMapping
    public Car createCar(@Valid @RequestBody Car car) {
        return carService.createCar(car);
    }

    @Operation(summary = "Update an existing car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the car",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "406 (v1)", description = "Error: The car with id 'XXXX' does not exists.",
                    content = @Content),
            @ApiResponse(responseCode = "406 (v2)", description = "Error: car with unique_id 'CXXX' already exists.",
                    content = @Content)
    })
    @PutMapping
    public Car updateCar(@Valid @RequestBody Car car) {
        return carService.updateCar(car);
    }

    @Operation(summary = "Delete an existing car by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the car"),
            @ApiResponse(responseCode = "406", description = "Error: The car with id 'XXXX' does not exists.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCar(@Parameter(description = "id of car to be deleted") @PathVariable("id") Long id) {
        carService.deleteCarById(id);
    }

    @Operation(summary = "Get the list off all cars.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Car.class)))})
    })
    @GetMapping("/list")
    public List<Car> listOfAllCars() { return carService.findAll(); }
}