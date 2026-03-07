package com.gesco.notification.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAddress {
    private final String value;

    public static EmailAddress of(String value) {
        if (value == null || !value.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        return new EmailAddress(value);
    }
}
