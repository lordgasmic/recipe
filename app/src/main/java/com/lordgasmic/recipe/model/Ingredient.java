package com.lordgasmic.recipe.model;

import com.lordgasmic.recipe.constants.Quantity;
import com.lordgasmic.recipe.constants.UnitOfMesure;

import lombok.Data;

@Data
public class Ingredient {

    private String recipeId;
    private Item item;
    private int quantity;
    private Quantity quantityCode;
    private UnitOfMesure uom;
    private int sequence;
}
