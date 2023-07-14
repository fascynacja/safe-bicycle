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
    private String make;
    private String model;
    private Coverage coverage;
    private int manufactureYear;
    private BigDecimal sumInsured;
    private List<String> risks;
}
