package com.ygor.security.events.manager.securityeventsmanager.dtos;

import com.ygor.security.events.manager.securityeventsmanager.entities.City;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "Field mandatory.")
    private String name;

    public CityDTO(City city) {
        this.id = city.getId();
        this.name = city.getName();
    }

}
