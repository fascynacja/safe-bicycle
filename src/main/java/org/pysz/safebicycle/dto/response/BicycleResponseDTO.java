package org.pysz.safebicycle.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pysz.safebicycle.dto.Coverage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BicycleResponseDTO {
    HashMap<String, Object> attributes;
    Coverage coverageType;
    List<RiskCalculationResponseDto> risks;
    @JsonSerialize(using = AmountSerializer.class)
    BigDecimal sumInsured;
    @JsonSerialize(using = AmountSerializer.class)
    BigDecimal premium;
}
