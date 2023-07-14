package org.pysz.safebicycle.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pysz.safebicycle.dto.Coverage;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BicycleDTO {
    String make;
    String model;
    Coverage coverage;
    int manufactureYear;
    BigDecimal sumInsured;
    List<String> risks;
}
