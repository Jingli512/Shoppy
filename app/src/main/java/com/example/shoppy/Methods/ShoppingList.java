package com.example.shoppy.Methods;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingList
{
    private String name;
    private HashMap<Recipe, Integer> recipes;
    private ArrayList<Ingredient> individualItems;
    private String comment;

    public ShoppingList(String name, HashMap<Recipe, Integer> recipes, ArrayList<Ingredient> individualItems, String comment)
    {
        this.name = name;
        this.recipes = recipes;
        this.individualItems = individualItems;

        this.comment = comment;
    }

    public ArrayList<Ingredient> getAllItems()
    {
        ArrayList<Ingredient> allItems = new ArrayList<>();

        ArrayList<Ingredient> itemsToMerge = new ArrayList<>();
        for (Recipe recipe: recipes.keySet())
        {
            itemsToMerge.addAll(recipe.forPeople(recipes.get(recipe)));
        }
        itemsToMerge.addAll(individualItems);

        for (Ingredient ingredient : itemsToMerge)
        {
            boolean found = false;
            for (Ingredient ingredient1: allItems)
            {
                if (ingredient.getName().equals(ingredient1.getName()))
                {
                    if (ingredient.getType().equals(ingredient1.getType()))
                    {
                        ingredient1.increaseQuantity(ingredient.getQuantity());
                    }else{
                        allItems.add(ingredient);
                    }
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                allItems.add(ingredient);
            }
        }

        return allItems;
    }

    public HashMap<Recipe, Integer> getRecipes()
    {
        return recipes;
    }

    public String getName() {
        return name;
    }

    public String getComment()
    {
        return comment;
    }
}
