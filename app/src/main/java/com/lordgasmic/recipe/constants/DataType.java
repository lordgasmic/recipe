package com.lordgasmic.recipe.constants;

/**
 * Created by atguser on 4/11/17.
 */
public enum DataType {

    STRING("string"), INT("int"), LIST("list"), ITEM("item"), UOM("uom");

    private String value;

    DataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DataType fromValue(String value) {
        for (DataType dt : DataType.values()) {
            if (dt.value.equals(value)) {
                return dt;
            }
        }

        return null;
    }
}
