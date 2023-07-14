package org.pysz.safebicycle.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pysz.safebicycle.dto.request.BicyclesDTO;
import org.pysz.safebicycle.dto.response.BicyclesResponseDTO;
import org.pysz.safebicycle.service.CalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class CalculationController {

    private final CalculationService service;

    @PostMapping("calculate")
    ResponseEntity<BicyclesResponseDTO> save(@RequestBody BicyclesDTO bicycles) {
        return ResponseEntity
                .status(200).
                body(service.calculate(bicycles));
    }


}
