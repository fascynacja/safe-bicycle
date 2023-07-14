package org.pysz.safebicycle.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskCalculationResponseDto {

    String riskType;
    @JsonSerialize(using = AmountSerializer.class)
    BigDecimal sumInsured;
    @JsonSerialize(using = AmountSerializer.class)
    BigDecimal premium;
}
