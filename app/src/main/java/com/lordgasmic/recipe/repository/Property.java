package com.lordgasmic.recipe.repository;

import com.lordgasmic.recipe.constants.DataType;

import lombok.Data;

/**
 * A property mapping between item descriptor and column
 *
 * Created by bruce on 3/2/17.
 */
@Data
public class Property {

    private String name;
    private String columnName;
    private DataType dataType;

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Property:");sb.append(System.lineSeparator());
        sb.append("  name: " + name); sb.append(System.lineSeparator());
        sb.append("  columnName: " + columnName); sb.append(System.lineSeparator());
        sb.append("  dataType: " + dataType); sb.append(System.lineSeparator());

        return sb.toString();
    }
}
