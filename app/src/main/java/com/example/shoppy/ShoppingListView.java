package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shoppy.Methods.ControlDB;
import com.example.shoppy.Methods.Ingredient;
import com.example.shoppy.Methods.Recipe;
import com.example.shoppy.Methods.ShoppingList;
import com.example.shoppy.Methods.Type;

public class ShoppingListView extends AppCompatActivity {

    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_view);

        database = openOrCreateDatabase("ShoppyDB",MODE_PRIVATE,null);

        Intent intent = getIntent();
        String title = intent.getStringExtra(MyShoppingLists.EXTRA_TITLE);
        TextView textView = findViewById(R.id.textView3);
        TextView titleView = findViewById(R.id.textView_slv);
        textView.setText(getShoppingListString(ControlDB.getShoppingList(database, title)));
        titleView.setText(title);
    }

    private String getShoppingListString(ShoppingList shoppingList)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(shoppingList.getRecipes().size() > 0){
            stringBuilder.append("Recipes Included:\n");
            for (Recipe recipe: shoppingList.getRecipes().keySet())
            {
                stringBuilder.append("-").append(recipe.getName());
                stringBuilder.append("( For ").append(shoppingList.getRecipes().get(recipe)).append(" People )\n");
            }

            stringBuilder.append("\nIngredients Needed:\n");
        }else{
            stringBuilder.append("\nItems Needed:\n");
        }

        for (Ingredient ingredient: shoppingList.getAllItems())
        {
            stringBuilder.append(getIngredientString(ingredient)).append("\n");
        }
        stringBuilder.append("\n").append("Comment:\n");
        stringBuilder.append("\n").append(shoppingList.getComment()).append("\n\n\n\n");

        return stringBuilder.toString();
    }

    private String getIngredientString(Ingredient ingredient)
    {
        if (ingredient.getType().equals(Type.L) ||
                ingredient.getType().equals(Type.ML) ||
                ingredient.getType().equals(Type.KG) ||
                ingredient.getType().equals(Type.G))
        {
            return "-" + ingredient.getName() + " x" + ingredient.getQuantity() + ingredient.getType();
        }else if (ingredient.getType().equals(Type.UNITS)){
            return "-" + ingredient.getName() + " x" + ingredient.getQuantity();
        }else{
            return "-" + ingredient.getName() + " x" + ingredient.getQuantity() +" "+ ingredient.getType();
        }
    }
}
