package org.pysz.safebicycle.rules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pysz.safebicycle.dto.Coverage;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class Bicycle {
    String make;
    String model;
    Coverage coverage;
    int manufactureYear;
    BigDecimal sumInsured;
    List<String> risks;

}
