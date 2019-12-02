package com.example.shoppy.Methods;

import java.util.ArrayList;

public class Recipe
{
    private String name;
    private ArrayList<Ingredient> ingredients;
    private int people;
    private String method;

    public Recipe(String name, ArrayList<Ingredient> ingredients, int people, String method)
    {
        this.name = name;
        this.ingredients = ingredients;
        this.people = people;
        this.method = method;
    }
    Recipe(String name)
    {
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.people = 0;
        this.method = "";
    }


    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getPeople() {
        return people;
    }

    ArrayList<Ingredient> forPeople(int people)
    {
        ArrayList<Ingredient> result = new ArrayList<>();
        double factor = ((double) people) / this.people;
        for (Ingredient ingredient: ingredients)
        {
            result.add(new Ingredient(ingredient, factor));
        }

        return result;
    }

    public String getMethod() {
        return method;
    }
}
