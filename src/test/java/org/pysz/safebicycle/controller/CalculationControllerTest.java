package org.pysz.safebicycle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pysz.safebicycle.dto.request.BicyclesDTO;
import org.pysz.safebicycle.dto.response.BicycleResponseDTO;
import org.pysz.safebicycle.dto.response.BicyclesResponseDTO;
import org.pysz.safebicycle.dto.response.RiskCalculationResponseDto;
import org.pysz.safebicycle.service.ApiException;
import org.pysz.safebicycle.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.pysz.safebicycle.testdata.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculationController.class)
class CalculationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculationService service;

    @Test
    void calculate() throws Exception {

        //given
        String json =
                """
                           {
                           "bicycles" : [
                             {
                               "make" : "Pearl",
                               "model" : "Gravel SL EVO",
                               "coverage" : "EXTRA",
                               "manufactureYear" : 2015,
                               "sumInsured" : 1000,
                               "risks" : [
                                 "THEFT",
                                 "DAMAGE",
                                 "THIRD_PARTY_DAMAGE"
                               ]
                             }
                             ]
                             }
                        """;
        BicyclesResponseDTO responseDTO = BicyclesResponseDTO.builder()
                .objects(bicyclesList())
                .premium(BICYCLES_ALL_PREMIUM)
                .build();
        BicyclesDTO bicycles = objectMapper.readValue(json, BicyclesDTO.class);
        Mockito.when(service.calculate(bicycles)).thenReturn(responseDTO);

        //when then
        this.mockMvc.perform(
                        post("/calculate")
                                .content(json)
                                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.objects[0].coverageType").value(RED_BICYCLE_COVERAGE.toString()))
                .andExpect(jsonPath("$.objects[0].sumInsured").value(round(RED_BICYCLE_SUM_INSURED)))
                .andExpect(jsonPath("$.objects[0].premium").value(round(RED_BICYCLE_PREMIUM)))
                .andExpect(jsonPath("$.objects[0].risks[0].riskType").value(RISK_TYPE_THEFT))
                .andExpect(jsonPath("$.objects[0].risks[0].riskType").value(RISK_TYPE_THEFT))
                .andExpect(jsonPath("$.premium").value(round(BICYCLES_ALL_PREMIUM)))
        ;
    }


    @Test
    void validationScenario() throws Exception {
        //given
        String json =
                """
                           {
                           "bicycles" : [
                             {
                               "make" : "Pearl",
                               "model" : "Gravel SL EVO",
                               "coverage" : "EXTRA",
                               "manufactureYear" : 2015,
                               "sumInsured" : 1000,
                               "risks" : [
                                 "THEFT",
                                 "DAMAGE",
                                 "THIRD_PARTY_DAMAGE"
                               ]
                             }
                             ]
                             }
                        """;

        BicyclesDTO bicycles = objectMapper.readValue(json, BicyclesDTO.class);
        Mockito.when(service.calculate(bicycles)).thenThrow(new ApiException("message"));

        //when then
        this.mockMvc.perform(
                        post("/calculate")
                                .content(json)
                                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

    private static String round(BigDecimal amount) {
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private List<BicycleResponseDTO> bicyclesList() {
        return List.of(BicycleResponseDTO.builder()
                .risks(risksList())
                .coverageType(RED_BICYCLE_COVERAGE)
                .attributes(attributes())
                .sumInsured(RED_BICYCLE_SUM_INSURED)
                .premium(RED_BICYCLE_PREMIUM)
                .build());
    }

    private HashMap<String, Object> attributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("MAKE", RED_BICYCLE_MAKE);
        attributes.put("MODEL", RED_BICYCLE_MODEL);
        attributes.put("MANUFACTURE_YEAR", RED_BICYCLE_MAKE_MANUFACTURE_YEAR);
        return attributes;
    }

    private List<RiskCalculationResponseDto> risksList() {
        return List.of(RiskCalculationResponseDto.builder()
                .riskType(RISK_TYPE_THEFT)
                .premium(RED_BICYCLE_PREMIUM)
                .sumInsured(RISK_THEFT_SUM_INSURED)
                .build());
    }


}