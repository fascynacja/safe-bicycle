package org.pysz.safebicycle.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.pysz.safebicycle.dto.request.BicycleDTO;
import org.pysz.safebicycle.dto.request.BicyclesDTO;
import org.pysz.safebicycle.dto.response.BicycleResponseDTO;
import org.pysz.safebicycle.dto.response.BicyclesResponseDTO;
import org.pysz.safebicycle.dto.response.RiskCalculationResponseDto;
import org.pysz.safebicycle.mapper.ProjectMapper;
import org.pysz.safebicycle.rules.RuleEngine;
import org.pysz.safebicycle.testdata.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.pysz.safebicycle.testdata.TestData.RISK_TYPE_THEFT;

@RunWith(SpringRunner.class)
@SpringBootTest
class CalculationServiceIntegrationTest {

    @Autowired
    RuleEngine ruleEngine;
    @Autowired
    ProjectMapper mapper;
    @Autowired
    CalculationService service;


    // TODO add more test scenarios
    @Test
    void calculate() {
        //given
        BicyclesDTO bicyclesDto = bicycles(validBicycle());

        //when
        BicyclesResponseDTO calculate = service.calculate(bicyclesDto);

        //then
        assertThat(calculate)
                .isNotNull()
                .returns(TestData.RED_BICYCLE_PREMIUM, BicyclesResponseDTO::getPremium)
                .extracting(BicyclesResponseDTO::getObjects)
                .extracting(list -> list.get(0))
                .returns(TestData.RED_BICYCLE_COVERAGE, BicycleResponseDTO::getCoverageType)
                .returns(TestData.RED_BICYCLE_PREMIUM, BicycleResponseDTO::getPremium)
                .returns(TestData.RED_BICYCLE_SUM_INSURED, BicycleResponseDTO::getSumInsured)
                .extracting(BicycleResponseDTO::getRisks)
                .extracting(risks -> risks.get(0))
                .returns(TestData.RED_BICYCLE_PREMIUM, RiskCalculationResponseDto::getPremium)
                .returns(TestData.RISK_THEFT_SUM_INSURED, RiskCalculationResponseDto::getSumInsured)
                .returns(RISK_TYPE_THEFT, RiskCalculationResponseDto::getRiskType)
        ;

    }

    @Test
    void tooOldBicycle() {
        //given
        BicyclesDTO bicyclesDto = bicycles(oldBicycle());

        //when
        //then
        assertThrows(ApiException.class, () -> service.calculate(bicyclesDto));
    }

    @Test
    void tooBigSumInsured() {
        //given
        BicyclesDTO bicyclesDto = bicycles(tooExpensiveBicycle());

        //when
        //then
        assertThrows(ApiException.class, () -> service.calculate(bicyclesDto));
    }

    private static BicyclesDTO bicycles(BicycleDTO bicycle) {
        return BicyclesDTO.builder()
                .bicycles(List.of(bicycle))
                .build();
    }


    private static BicycleDTO validBicycle() {
        return BicycleDTO.builder()
                .risks(risks())
                .model(TestData.RED_BICYCLE_MODEL)
                .make(TestData.RED_BICYCLE_MAKE)
                .coverage(TestData.RED_BICYCLE_COVERAGE)
                .manufactureYear(TestData.RED_BICYCLE_MAKE_MANUFACTURE_YEAR)
                .sumInsured(TestData.RED_BICYCLE_SUM_INSURED)
                .build();
    }

    private static BicycleDTO oldBicycle() {
        return BicycleDTO.builder()
                .risks(risks())
                .model(TestData.RED_BICYCLE_MODEL)
                .make(TestData.RED_BICYCLE_MAKE)
                .coverage(TestData.RED_BICYCLE_COVERAGE)
                .manufactureYear(2000)
                .sumInsured(TestData.RED_BICYCLE_SUM_INSURED)
                .build();
    }

    private static BicycleDTO tooExpensiveBicycle() {
        return BicycleDTO.builder()
                .risks(risks())
                .model(TestData.RED_BICYCLE_MODEL)
                .make(TestData.RED_BICYCLE_MAKE)
                .coverage(TestData.RED_BICYCLE_COVERAGE)
                .manufactureYear(TestData.RED_BICYCLE_MAKE_MANUFACTURE_YEAR)
                .sumInsured(new BigDecimal("20000"))
                .build();
    }

    private static List<String> risks() {
        return List.of(RISK_TYPE_THEFT);
    }

}