package com.example.shoppy.Methods;

public class Ingredient
{
    private String name;
    private double quantity;
    private Type type;

    public Ingredient(String name, double quantity, Type type)
    {
        this.name = name;
        this.quantity = quantity;
        this.type = type;
    }

    Ingredient(Ingredient ingredient, double factor)
    {
        this.name = ingredient.getName();
        this.quantity = Math.round((ingredient.getQuantity() * factor) * 10) / 10.0;
        this.type = ingredient.getType();
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public Type getType() {
        return type;
    }

    void increaseQuantity(double value)
    {
        this.quantity += value;
    }
}
