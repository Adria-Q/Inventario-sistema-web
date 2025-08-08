package com.example_inventario.Model;

import lombok.Getter;

@Getter

public enum Categoria {
    ROPA("Ropa"),
    CALZADO("Calzado"),
    ACCESORIOS("Accesorios");

    private final String displayName;

    Categoria(String displayName) {
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
