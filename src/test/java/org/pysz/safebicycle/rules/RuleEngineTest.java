package org.pysz.safebicycle.rules;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.pysz.safebicycle.rules.model.Bicycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class RuleEngineTest {

    @Autowired
    RuleEngine ruleEngine;

    @Test
    void premiumForRiskTHEFT() {

        //given
        Bicycle bicycle = bicycle();

        //when
        BigDecimal premium = ruleEngine.premiumForRisk("THEFT", bicycle);

        //then
        assertThat(premium).isEqualTo(new BigDecimal("30.00"));
    }


    @Test
    void premiumForRisk_DAMAGE() {
        //given
        Bicycle bicycle = bicycle();

        //when
        BigDecimal premium = ruleEngine.premiumForRisk("DAMAGE", bicycle);

        //then
        assertThat(premium.setScale(2, BigDecimal.ROUND_HALF_UP)).isEqualTo(new BigDecimal("6.95"));
    }

    @Test
    void premiumForRisk_THIRD_PARTY_DAMAGE() {
        //given
        Bicycle bicycle = bicycle();

        //when
        BigDecimal premium = ruleEngine.premiumForRisk("THIRD_PARTY_DAMAGE", bicycle);

        //then
        assertThat(premium).isEqualTo(new BigDecimal("12.0"));
    }

    @Test
    void sumInsuredForRiskTHEFT() {

        //given
        Bicycle bicycle = bicycle();

        //when
        BigDecimal sumInsured = ruleEngine.sumInsuredForRisk("THEFT", bicycle);

        //then
        assertThat(sumInsured).isEqualTo(new BigDecimal("1000.0"));
    }


    @Test
    void sumInsuredForRisk_DAMAGE() {
        //given
        Bicycle bicycle = bicycle();

        //when
        BigDecimal sumInsured = ruleEngine.sumInsuredForRisk("DAMAGE", bicycle);

        //then
        assertThat(sumInsured.setScale(2, BigDecimal.ROUND_HALF_UP)).isEqualTo(new BigDecimal("500.00"));
    }


    @Test
    void sumInsuredForRisk_THIRD_PARTY_DAMAGE() {
        //given
        Bicycle bicycle = bicycle();

        //when
        BigDecimal sumInsured = ruleEngine.sumInsuredForRisk("THIRD_PARTY_DAMAGE", bicycle);

        //then
        assertThat(sumInsured).isEqualTo(new BigDecimal("100.0"));
    }


    private static Bicycle bicycle() {
        return Bicycle.builder()
                .make("Pearl")
                .model("Gravel SL EVO")
                .manufactureYear(2015)
                .sumInsured(new BigDecimal("1000.0"))
                .risks(List.of("THEFT", "DAMAGE", "THIRD_PARTY_DAMAGE"))
                .build();
    }


}