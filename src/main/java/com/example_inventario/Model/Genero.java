package com.example_inventario.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Genero {
    HOMBRE("Hombre"),
    MUJER("Mujer"),
    NIÑO("Niño"),
    NIÑA("Niña"),
    BEBES("Bebés");

    private final String displayName;

    Genero(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
