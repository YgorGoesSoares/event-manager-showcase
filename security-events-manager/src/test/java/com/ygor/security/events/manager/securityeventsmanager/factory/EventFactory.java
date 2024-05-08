package com.ygor.security.events.manager.securityeventsmanager.factory;

import com.ygor.security.events.manager.securityeventsmanager.entities.City;
import com.ygor.security.events.manager.securityeventsmanager.entities.Event;

import java.time.Instant;
import java.time.LocalDate;

public class EventFactory {
    public static Event createEvent() {
        LocalDate date = LocalDate.now().plusDays(10);
        return new Event(1L, "Event Test", date, "eventurl.com", new City(1L, "New Gotham"));
    }
}
