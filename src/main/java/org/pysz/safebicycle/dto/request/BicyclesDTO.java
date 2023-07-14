package org.pysz.safebicycle.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pysz.safebicycle.dto.request.BicycleDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BicyclesDTO
{
    List<BicycleDTO> bicycles;
}
