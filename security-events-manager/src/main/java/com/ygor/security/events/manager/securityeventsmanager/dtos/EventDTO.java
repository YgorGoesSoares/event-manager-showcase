package com.ygor.security.events.manager.securityeventsmanager.dtos;

import com.ygor.security.events.manager.securityeventsmanager.entities.City;
import com.ygor.security.events.manager.securityeventsmanager.entities.Event;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @NotBlank(message = "Name cannot be null.")
    private String name;
    @FutureOrPresent(message = "Date cannot be past.")
    private LocalDate date;
    private String url;
    @NotNull(message = "City cannot be null.")
    private City city;


    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
        this.url = event.getUrl();
        this.city = event.getCity();
    }
}