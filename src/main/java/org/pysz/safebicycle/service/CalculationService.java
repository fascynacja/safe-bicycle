package org.pysz.safebicycle.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pysz.safebicycle.dto.request.BicycleDTO;
import org.pysz.safebicycle.dto.request.BicyclesDTO;
import org.pysz.safebicycle.dto.response.BicycleResponseDTO;
import org.pysz.safebicycle.dto.response.BicyclesResponseDTO;
import org.pysz.safebicycle.dto.response.RiskCalculationResponseDto;
import org.pysz.safebicycle.mapper.ProjectMapper;
import org.pysz.safebicycle.rules.RuleEngine;
import org.pysz.safebicycle.rules.model.Bicycle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CalculationService {
    private static final BigDecimal MAX_SUM_INSURED = new BigDecimal("10000"); // TODO put is as property
    private static final int AGE_LIMIT = 10; // TODO put is as property
    private final RuleEngine ruleEngine;
    private final ProjectMapper mapper;

    public BicyclesResponseDTO calculate(BicyclesDTO bicyclesDTO) {
        log.info("Calculating premium and sum insured for bicycles: " + bicyclesDTO);
        validate(bicyclesDTO);
        List<BicycleResponseDTO> bicycles = bicyclesDTO.getBicycles().stream()
                .map(this::bicycleCalculation)
                .collect(Collectors.toList());
        return BicyclesResponseDTO.builder()
                .objects(bicycles)
                .premium(premium(bicycles))
                .build();
    }

    private void validate(BicyclesDTO bicyclesDTO) {
        int currentYear = LocalDate.now().getYear();
        bicyclesDTO.getBicycles().stream()
                .filter(bicycleDTO -> currentYear - bicycleDTO.getManufactureYear() > AGE_LIMIT)
                .findAny()
                .ifPresent(oldBicycle -> {
                    ApiException apiException = new ApiException("Age for bicycle  [" + oldBicycle.getMake() + " - " + oldBicycle.getModel() + "] is bigger than age limit: " + AGE_LIMIT);
                    log.warn(apiException.getMessage());
                    throw apiException;
                });

        bicyclesDTO.getBicycles().stream()
                .filter(bicycleDTO -> bicycleDTO.getSumInsured().compareTo(MAX_SUM_INSURED) >= 0)
                .findAny()
                .ifPresent(bicycle -> {
                    ApiException apiException = new ApiException("Sum insured for bicycle  [" + bicycle.getMake() + " - " + bicycle.getModel() + "] is bigger than limit: " + MAX_SUM_INSURED);
                    log.warn(apiException.getMessage());
                    throw apiException;
                });
    }

    private BicycleResponseDTO bicycleCalculation(BicycleDTO bicycle) {
        List<RiskCalculationResponseDto> risks = risks(bicycle);
        return BicycleResponseDTO.builder()
                .risks(risks)
                .coverageType(bicycle.getCoverage())
                .attributes(attributes(bicycle))
                .sumInsured(bicycle.getSumInsured())
                .premium(getPremiumForBicycle(risks))
                .build();
    }

    private static BigDecimal getPremiumForBicycle(List<RiskCalculationResponseDto> risks) {
        return risks.stream()
                .map(RiskCalculationResponseDto::getPremium)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private HashMap<String, Object> attributes(BicycleDTO bicycle) {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("MAKE", bicycle.getMake());
        attributes.put("MODEL", bicycle.getModel());
        attributes.put("MANUFACTURE_YEAR", bicycle.getManufactureYear());
        return attributes;
    }

    private List<RiskCalculationResponseDto> risks(BicycleDTO bicycle) {
        return bicycle.getRisks()
                .stream()
                .map(risk -> riskCalculation(bicycle, risk))
                .collect(Collectors.toList());
    }

    private static BigDecimal premium(List<BicycleResponseDTO> bicycleCalculations) {
        return bicycleCalculations.stream()
                .map(BicycleResponseDTO::getPremium)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    private RiskCalculationResponseDto riskCalculation(BicycleDTO bicycleDto, String risk) {
        Bicycle bicycle = mapper.fromDto(bicycleDto);
        return RiskCalculationResponseDto.builder()
                .premium(ruleEngine.premiumForRisk(risk, bicycle))
                .sumInsured(ruleEngine.sumInsuredForRisk(risk, bicycle))
                .riskType(risk)
                .build();
    }


}
