package com.example_inventario.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Rol {
    CLIENTE("Cliente"),
    ADMIN("Administrador");

    private final String displayName;

    Rol(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Esta anotación le dice a Jackson cómo debe serializar el enum
    @JsonValue
    public String toValue() {
        return this.name(); // devuelve "ADMIN" o "CLIENTE"
    }

    // Esta anotación le dice a Jackson cómo deserializar desde texto plano
    @JsonCreator
    public static Rol fromValue(String value) {
        return Rol.valueOf(value.toUpperCase());
    }

    @Override
    public String toString() {
        return displayName;
    }
}