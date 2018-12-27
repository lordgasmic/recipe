package com.lordgasmic.recipe.model;

import java.util.List;

import lombok.Data;

@Data
public class Recipe {

    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Steps> steps;
}
