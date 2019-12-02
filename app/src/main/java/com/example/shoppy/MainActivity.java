package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.shoppy.Methods.ControlDB;
import com.example.shoppy.Methods.Ingredient;
import com.example.shoppy.Methods.Recipe;
import com.example.shoppy.Methods.Type;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase database = openOrCreateDatabase("ShoppyDB", MODE_PRIVATE, null);
        ControlDB.initTables(database);

        if (!ControlDB.isInitialized(database))
        {
            ControlDB.setAsInitialized(database);
            ControlDB.addDefaultRecipes(this, database);
        }
    }

    public void goToNewRecipe (View view){
        Intent intent = new Intent(this, NewRecipe.class);
        startActivity(intent);
    }

    public void  goToNewShoppingList(View view){
        Intent intent = new Intent(this, NewShoppingList.class);
        startActivity(intent);
    }

    public void  goToMyRecipes(View view){
        Intent intent = new Intent(this, MyRecipes.class);
        startActivity(intent);
    }

    public void  goToMyShoppingLists(View view){
        Intent intent = new Intent(this, MyShoppingLists.class);
        startActivity(intent);
    }

    public void  goToMashup(View view){
        Intent intent = new Intent(this, Mashup.class);
        startActivity(intent);
    }
}
