package com.lordgasmic.recipe.model;

import lombok.Data;

@Data
public class Steps {

    private String recipeId;
    private String step;
    private int sequence;
}
