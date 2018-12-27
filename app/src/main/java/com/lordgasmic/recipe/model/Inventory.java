package com.lordgasmic.recipe.model;

import com.lordgasmic.recipe.constants.Quantity;
import com.lordgasmic.recipe.constants.UnitOfMesure;

import lombok.Data;

@Data
public class Inventory {

    private Item item;
    private int onHandQuantity;
    private Quantity onHandQuantityCode;
    private UnitOfMesure onHandUom;
    private int parQuantity;
    private Quantity parQuantityCode;
    private UnitOfMesure parUom;
}
