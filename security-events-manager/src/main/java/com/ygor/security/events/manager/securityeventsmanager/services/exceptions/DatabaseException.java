package com.ygor.security.events.manager.securityeventsmanager.services.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class DatabaseException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public DatabaseException(String msg) {
        super(msg);
    }
}
