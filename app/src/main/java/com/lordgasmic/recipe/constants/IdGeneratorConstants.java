package com.lordgasmic.recipe.constants;

/**
 * Constants for id_generator
 *
 * Created by neo on 4/13/17.
 */
public class IdGeneratorConstants {

    public static final String TABLE_NAME = "id_generator";

    public static final String ID_SPACE_NAME = "id_space_name";

    public static final String ID_SPACE = "id_space";

    public static final String PREFIX = "prefix";

    public static final String SUFFIX = "suffix";

    public static final String GET_ID_WHERE = ID_SPACE_NAME + " = ?";

    private IdGeneratorConstants() {}
}
