package com.ygor.security.events.manager.securityeventsmanager.factory;

import com.ygor.security.events.manager.securityeventsmanager.entities.City;

public class CityFactory {
    public static City createCity(){
        return new City(1L, "São Paulo");
    }
}
