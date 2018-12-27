package com.lordgasmic.recipe.constants;

public enum Quantity {
    QUARTER("1/4"), HALF("1/2"), THREE_QUARTERS("3/4");

    private String code;
    Quantity(String code) {
        this.code = code;
    }
}
