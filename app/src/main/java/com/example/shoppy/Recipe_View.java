package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shoppy.Methods.ControlDB;
import com.example.shoppy.Methods.Ingredient;
import com.example.shoppy.Methods.Recipe;
import com.example.shoppy.Methods.Type;

public class Recipe_View extends AppCompatActivity {

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__view);

        database = openOrCreateDatabase("ShoppyDB",MODE_PRIVATE,null);

        Intent intent = getIntent();
        String title = intent.getStringExtra(MyShoppingLists.EXTRA_TITLE);
        TextView textView = findViewById(R.id.textView3);
        TextView titleView = findViewById(R.id.textView_slv);
        textView.setText(getRecipeString(ControlDB.getRecipe(database, title)));
        titleView.setText(title);
    }

    private String getRecipeString(Recipe recipe)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nIngredients Needed").append(" For ").append(recipe.getPeople()).append(" People:\n");

        for (Ingredient ingredient: recipe.getIngredients())
        {
            stringBuilder.append(getIngredientString(ingredient)).append("\n");
        }
        stringBuilder.append("\nMethod:\n");
        stringBuilder.append(recipe.getMethod()).append("\n\n\n");

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
