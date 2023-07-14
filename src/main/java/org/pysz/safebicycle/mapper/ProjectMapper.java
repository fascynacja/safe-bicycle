package org.pysz.safebicycle.mapper;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pysz.safebicycle.dto.request.BicycleDTO;
import org.pysz.safebicycle.rules.model.Bicycle;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectMapper {

    private final ModelMapper modelMapper;

    public Bicycle fromDto(BicycleDTO bicycleDto) {
        return modelMapper.map(bicycleDto, Bicycle.class);
    }
}