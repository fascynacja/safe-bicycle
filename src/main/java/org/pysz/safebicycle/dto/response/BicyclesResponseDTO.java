package org.pysz.safebicycle.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BicyclesResponseDTO {
    List<BicycleResponseDTO> objects;
    @JsonSerialize(using = AmountSerializer.class)
    BigDecimal premium;
}
