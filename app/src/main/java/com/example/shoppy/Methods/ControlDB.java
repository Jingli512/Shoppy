package com.example.shoppy.Methods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControlDB
{
    public static void initTables(SQLiteDatabase database)
    {
        database.execSQL("CREATE TABLE IF NOT EXISTS ShoppingList(Name VARCHAR, Comment VARCHAR);");
        database.execSQL("CREATE TABLE IF NOT EXISTS ShoppingListRecipes(Name VARCHAR, RecipeName VARCHAR, People INTEGER);");
        database.execSQL("CREATE TABLE IF NOT EXISTS ShoppingListItems(Name VARCHAR, ItemName VARCHAR, Quantity REAL, Type VARCHAR);");
        database.execSQL("CREATE TABLE IF NOT EXISTS Recipe(Name VARCHAR, People INTEGER, Method VARCHAR);");
        database.execSQL("CREATE TABLE IF NOT EXISTS Ingredient(Recipe VARCHAR, Name VARCHAR, Quantity REAL, Type VARCHAR);");
        database.execSQL("CREATE TABLE IF NOT EXISTS Options(Initialized INTEGER);");
    }

    public static void addDefaultRecipes(Context context, SQLiteDatabase database)
    {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Spaghetti", 500, Type.G));
        ingredients.add(new Ingredient("Eggs", 4, Type.UNITS));
        ingredients.add(new Ingredient("Black pepper", 1, Type.TSP));
        ingredients.add(new Ingredient("Pecorino Romano", 3, Type.TSP));
        ingredients.add(new Ingredient("Single cream", 10, Type.ML));
        ingredients.add(new Ingredient("Extra-virgin olive oil", 1, Type.TBSP));
        ingredients.add(new Ingredient("Pancetta", 3, Type.SLICES));
        Recipe recipe = new Recipe("Spaghetti alla Carbonara", ingredients, 4, "1) Boil the spaghetti in salted water until it is al dente. Drain and set aside.\n" +
                "\n" +
                "2) Beat the eggs. Add the black pepper and cheese to the beaten eggs. Set aside. Add the cream to this mixture, if desired, for a creamier dish.\n" +
                "\n" +
                "3) Put the oil in a saucepan with the pancetta, and saute for 5 minutes. Add the spaghetti into the pan and saute for another 3 minutes.\n" +
                "\n" +
                "4) Turn off the flame (this is important) and add the egg and cheese mixture to the pasta and mix.\n" +
                "\n" +
                "5) Serve with additional Pecorino Romano on top. ");
        ControlDB.addRecipesToDB(context, database, recipe);

        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Roasting chicken", 3, Type.KG));
        ingredients.add(new Ingredient("Fresh thyme", 1, Type.UNITS));
        ingredients.add(new Ingredient("Lemons", 4, Type.UNITS));
        ingredients.add(new Ingredient("Pecorino Romano", 3, Type.TSP));
        ingredients.add(new Ingredient("Garlic", 3, Type.UNITS));
        ingredients.add(new Ingredient("Butter", 30, Type.G));
        ingredients.add(new Ingredient("Streaky bacon rashers", 225, Type.G));
        ingredients.add(new Ingredient("White wine", 240, Type.ML));
        ingredients.add(new Ingredient("Chicken stock", 120, Type.ML));
        recipe = new Recipe("Roast Chicken", ingredients, 3, "1) Preheat the oven to 220°C/Gas mark 7. Remove the chicken giblets. Rinse the chicken inside and out. Remove any excess fat and leftover pinfeathers and pat the outside dry. Place the chicken in a large roasting tray.\n" +
                "\n" +
                "2) Liberally salt and pepper the inside of the chicken. Stuff the cavity with the thyme (reserving enough thyme to garnish the chicken dish), one lemon, halved, and two halves of the heads of garlic. Brush the outside of the chicken with the butter and sprinkle again with salt and pepper.\n" +
                "\n" +
                "3) Tie the legs together with kitchen string and tuck the wing tips under the body of the chicken. Cut two of the lemons in quarters and scatter the quarters and remaining garlic around the chicken. Lay the streaky bacon rashers over the chicken to cover.\n" +
                "\n" +
                "4) Roast the chicken for one hour. Remove the bacon from the top of the chicken and set aside. Continue roasting the chicken for an additional 30 minutes, or until the juices run clear when you cut between a leg and a thigh. Transfer to a platter and cover with aluminium foil while you prepare the gravy.\n" +
                "\n" +
                "5) Remove all but two tbsp of the fat from the bottom of the pan. Add the wine and chicken stock and bring it to a boil. Reduce the heat and simmer for five minutes, or until reduced by half.\n" +
                "\n" +
                "6) Slice the chicken on a platter. Garnish the chicken with the bacon slices, roasted garlic, reserved thyme and one lemon, sliced. Serve with the gravy. ");
        ControlDB.addRecipesToDB(context, database, recipe);

        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Οlive oil", 2, Type.TBSP));
        ingredients.add(new Ingredient("Garlic cloves, crushed", 2, Type.UNITS));
        ingredients.add(new Ingredient("Mushrooms, sliced", 100, Type.G));
        ingredients.add(new Ingredient("Red pepper, sliced", 1.5, Type.UNITS));
        ingredients.add(new Ingredient("Swiss chard", 100, Type.G));
        ingredients.add(new Ingredient("Butter", 30, Type.G));
        ingredients.add(new Ingredient("Fresh thyme, chopped", 1, Type.TBSP));
        ingredients.add(new Ingredient("Fresh salmon fillets, skin off", 500, Type.G));
        ingredients.add(new Ingredient("sugar", 140, Type.G));
        ingredients.add(new Ingredient("Lemon", 1.5, Type.UNITS));
        recipe = new Recipe("Stuffed Baked Salmon", ingredients, 2,
                "Preheat the oven at 200°C and line a baking tray with greased foil.\n" +
                "\n" +
                "Heat the oil and sauté the garlic for a minute.\n" +
                "\n" +
                "Add the mushrooms and pepper and cook until slightly soft.\n" +
                "\n" +
                "Add the Swiss chard, thyme and lemon zest and cook until wilted. Season and leave to cool slightly.\n" +
                "\n" +
                "Make a lengthwise slit on the fish fillets (keeping both ends in tact) to make room for the stuffing.\n" +
                "\n" +
                "Stuff half of the stuffing into each fillet and place in a baking sheet lined with foil.\n" +
                "\n" +
                "Bake in oven for 10 minutes.\n" +
                "\n" +
                "Add the sugar snaps and bake for a further 2 minutes of cooking until the fish flakes easily when tested with a fork. The fish should still be pink inside.\n" +
                "\n" +
                "Transfer to serving platter and finish with a squeeze of lemon juice.");
        ControlDB.addRecipesToDB(context, database, recipe);
    }

    public static boolean isInitialized(SQLiteDatabase database)
    {
        Cursor resultSetRecipe = database.rawQuery("Select * from Options;",null);
        resultSetRecipe.moveToFirst();
        boolean isInitialized = resultSetRecipe.getCount() > 0;
        resultSetRecipe.close();

        return isInitialized;
    }
    public static void setAsInitialized(SQLiteDatabase database)
    {
        database.execSQL("DELETE from Options;");
        database.execSQL("INSERT INTO Options VALUES('1');");
    }

    public static void addRecipesToDB(Context context, SQLiteDatabase database, Recipe recipe)
    {
        Cursor resultSetRecipe = database.rawQuery("Select * from Recipe WHERE name='"+recipe.getName()+"';",null);
        resultSetRecipe.moveToFirst();
        boolean alreadyExists = resultSetRecipe.getCount() > 0;
        resultSetRecipe.close();

        if (!alreadyExists)
        {
            for (Ingredient ingredient: recipe.getIngredients())
            {
                database.execSQL("INSERT INTO Ingredient VALUES('"+recipe.getName()+"', '"+ingredient.getName()+"', '"+ingredient.getQuantity()+"', '"+ingredient.getType().toString()+"');");
            }
            database.execSQL("INSERT INTO Recipe VALUES('"+recipe.getName()+"', '"+recipe.getPeople()+"', '"+recipe.getMethod()+"');");
        }else{
            Toast.makeText(context, "Unable to create \""+recipe.getName()+"\" recipe.\nRecipe with the same name already exists", Toast.LENGTH_LONG).show();
        }

    }
    public static void addShoppingListToDB(Context context, SQLiteDatabase database, ShoppingList shoppingList)
    {
        Cursor resultSetRecipe = database.rawQuery("Select * from ShoppingList WHERE name='" + shoppingList.getName() + "';", null);
        resultSetRecipe.moveToFirst();
        boolean alreadyExists = resultSetRecipe.getCount() > 0;
        resultSetRecipe.close();

        if (!alreadyExists)
        {
            for (Ingredient item : shoppingList.getAllItems()) {
                database.execSQL("INSERT INTO ShoppingListItems VALUES('" + shoppingList.getName() + "', '" + item.getName() + "', '" + item.getQuantity() + "', '" + item.getType().toString() + "');");
            }
            for (Recipe recipe: shoppingList.getRecipes().keySet())
            {
                database.execSQL("INSERT INTO ShoppingListRecipes VALUES('" + shoppingList.getName() + "', '" + recipe.getName() + "', '" + shoppingList.getRecipes().get(recipe) + "');");
            }
            database.execSQL("INSERT INTO ShoppingList VALUES('" + shoppingList.getName() + "', '" + shoppingList.getComment() + "');");
        }else{
            Toast.makeText(context, "Unable to create \""+shoppingList.getName()+"\" shopping list.\nShopping List with the same name already exists", Toast.LENGTH_LONG).show();
        }
    }

    public static List<String> getRecipes(SQLiteDatabase database)
    {
        List<String> spinnerArray = new ArrayList<>();

        Cursor resultSetRecipe = database.rawQuery("Select Name from Recipe;", null);
        resultSetRecipe.moveToFirst();
        for (int i = 0; i < resultSetRecipe.getCount(); i++)
        {
            String name = resultSetRecipe.getString(0);
            spinnerArray.add(name);
            resultSetRecipe.moveToNext();
        }
        resultSetRecipe.close();

        return spinnerArray;
    }

    public static Recipe getRecipe(SQLiteDatabase database, String recipeName)
    {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Cursor resultSetRecIngredients = database.rawQuery("Select * from Ingredient WHERE Recipe = '"+recipeName+"';", null);
        resultSetRecIngredients.moveToFirst();
        for (int i = 0; i < resultSetRecIngredients.getCount(); i++)
        {
            String itemName = resultSetRecIngredients.getString(1);
            double quantity = resultSetRecIngredients.getDouble(2);
            String type = resultSetRecIngredients.getString(3);
            ingredients.add(new Ingredient(itemName, quantity, Type.getType(type)));
            resultSetRecIngredients.moveToNext();
        }
        resultSetRecIngredients.close();

        Cursor resultSetRecipe = database.rawQuery("Select * from Recipe WHERE Name = '"+recipeName+"';", null);
        resultSetRecipe.moveToFirst();
        int people = resultSetRecipe.getInt(1);
        String method = resultSetRecipe.getString(2);
        resultSetRecipe.close();

        return new Recipe(recipeName, ingredients, people, method);
    }

    public static ShoppingList getShoppingList(SQLiteDatabase database, String shoppingListName)
    {
        HashMap<Recipe, Integer> recipes = new HashMap<>();
        ArrayList<Ingredient> allItems = new ArrayList<>();
        Cursor resultSetShoppingItems = database.rawQuery("Select * from ShoppingListItems WHERE Name = '"+shoppingListName+"';", null);
        resultSetShoppingItems.moveToFirst();
        for (int i = 0; i < resultSetShoppingItems.getCount(); i++)
        {
            String itemName = resultSetShoppingItems.getString(1);
            double quantity = resultSetShoppingItems.getDouble(2);
            String type = resultSetShoppingItems.getString(3);
            allItems.add(new Ingredient(itemName, quantity, Type.getType(type)));
            resultSetShoppingItems.moveToNext();
        }
        resultSetShoppingItems.close();

        Cursor resultSetShoppingRecipe = database.rawQuery("Select * from ShoppingListRecipes WHERE Name = '"+shoppingListName+"';", null);
        resultSetShoppingRecipe.moveToFirst();
        for (int i = 0; i < resultSetShoppingRecipe.getCount(); i++)
        {
            String recipeName = resultSetShoppingRecipe.getString(1);
            int people = resultSetShoppingRecipe.getInt(2);
            recipes.put(new Recipe(recipeName), people);
            resultSetShoppingRecipe.moveToNext();
        }
        resultSetShoppingRecipe.close();

        Cursor resultSetShoppingComment = database.rawQuery("Select * from ShoppingList WHERE Name = '"+shoppingListName+"';", null);
        resultSetShoppingComment.moveToFirst();
        String comment = resultSetShoppingComment.getString(1);
        resultSetShoppingComment.close();

        return new ShoppingList(shoppingListName, recipes, allItems, comment);
    }

    public static List<String> getShoppingLists(SQLiteDatabase database)
    {
        List<String> shoppingLists = new ArrayList<>();
        Cursor resultSetShoppingList = database.rawQuery("Select * from ShoppingList;", null);
        resultSetShoppingList.moveToFirst();
        for (int i = 0; i < resultSetShoppingList.getCount(); i++)
        {
            shoppingLists.add(resultSetShoppingList.getString(0));
            resultSetShoppingList.moveToNext();
        }
        resultSetShoppingList.close();

        return shoppingLists;
    }

    public static void removeShoppingList(SQLiteDatabase database, String name)
    {
        database.execSQL("DELETE FROM ShoppingList WHERE Name = '"+name+"';");
        database.execSQL("DELETE FROM ShoppingListRecipes WHERE Name = '"+name+"';");
        database.execSQL("DELETE FROM ShoppingListItems WHERE Name = '"+name+"';");
    }

    public static void removeRecipe(SQLiteDatabase database, String name)
    {
        database.execSQL("DELETE FROM Recipe WHERE Name = '"+name+"';");
        database.execSQL("DELETE FROM Ingredient WHERE Recipe = '"+name+"';");
    }
}
