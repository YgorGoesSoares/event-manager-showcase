package com.ygor.security.events.manager.securityeventsmanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_EVENT")
@EqualsAndHashCode(of = "id")
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate date;
    private String url;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
