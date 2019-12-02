package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppy.Methods.ControlDB;
import com.example.shoppy.Methods.Ingredient;
import com.example.shoppy.Methods.Recipe;
import com.example.shoppy.Methods.SelectRecComponent;
import com.example.shoppy.Methods.ShoppingList;
import com.example.shoppy.Methods.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewShoppingList extends AppCompatActivity {

    List<String> spinnerArray;
    List<SelectRecComponent> selectRecGroup = new ArrayList<>();
    TableLayout itemTable;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping_list);

        database = openOrCreateDatabase("ShoppyDB",MODE_PRIVATE,null);

        spinnerArray = ControlDB.getRecipes(database);

        itemTable = findViewById(R.id.itemTable);
    }


    public void createNewShoppingList(View view)
    {
        HashMap<Recipe, Integer> recipes = new HashMap<>();
        ArrayList<Ingredient> individualItems = getIndividualItems();

        String listName = ((EditText) findViewById(R.id.listName)).getText().toString();

        if (!listName.equals(""))
        {
            if (listAlreadyExists(listName))
            {
                Toast.makeText(this, "There already exists a Shopping List with the same name", Toast.LENGTH_SHORT).show();
            } else {
                LinearLayout recipesContainer = findViewById(R.id.selectRecipeContainer);
                int numOfRecipes = recipesContainer.getChildCount();
                for (int i = 1; i < numOfRecipes; i++) {
                    LinearLayout recipeContainer = (LinearLayout) recipesContainer.getChildAt(i);
                    String recipeName = String.valueOf(((Spinner) recipeContainer.getChildAt(1)).getSelectedItem());
                    int people = ((SeekBar) ((LinearLayout) recipeContainer.getChildAt(2)).getChildAt(2)).getProgress();

                    if (people > 0)
                    {
                        Recipe recipe = new Recipe(recipeName, getIngredients(recipeName), getPeople(recipeName), getMethod(recipeName));
                        recipes.put(recipe, people);
                    }
                }

                if (recipes.size() > 0 || individualItems.size() > 0)
                {
                    ShoppingList shoppingList = new ShoppingList(listName, recipes, individualItems, getComment());
                    ControlDB.addShoppingListToDB(this, database, shoppingList);

                    resetFields();

                    Toast.makeText(this, "New Shopping List Added To \"My Shopping Lists\"", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "No Recipes and Individual items selected", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            ViewCompat.setBackgroundTintList(findViewById(R.id.listName), ColorStateList.valueOf(Color.RED));
            findViewById(R.id.listName).requestFocus();
            Toast.makeText(this, "Enter the name of the Shopping List", Toast.LENGTH_LONG).show();
        }
    }

    private int getPeople(String recipeName)
    {
        Cursor resultSetRecipe = database.rawQuery("Select * from Recipe WHERE name='" + recipeName + "';", null);
        resultSetRecipe.moveToFirst();
        int recipePeople = resultSetRecipe.getInt(1);
        resultSetRecipe.close();

        return recipePeople;
    }
    private String getMethod(String recipeName)
    {
        Cursor resultSetRecipe = database.rawQuery("Select * from Recipe WHERE name='" + recipeName + "';", null);
        resultSetRecipe.moveToFirst();
        String method = resultSetRecipe.getString(2);
        resultSetRecipe.close();

        return method;
    }
    private ArrayList<Ingredient> getIngredients(String recipeName)
    {
        Cursor resultSetIngredients = database.rawQuery("Select * from Ingredient WHERE Recipe='" + recipeName + "';", null);
        resultSetIngredients.moveToFirst();

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        int numOfIngredients = resultSetIngredients.getCount();
        for (int j = 0; j < numOfIngredients; j++) {
            String name = resultSetIngredients.getString(1);
            double quantity = resultSetIngredients.getDouble(2);
            String type = resultSetIngredients.getString(3);

            ingredients.add(new Ingredient(name, quantity, Type.getType(type)));
            if (j + 1 < numOfIngredients) {
                resultSetIngredients.moveToNext();
            }
        }
        resultSetIngredients.close();

        return ingredients;
    }
    private ArrayList<Ingredient> getIndividualItems()
    {
        ArrayList<Ingredient> individualItems = new ArrayList<>();
        int numOfIndividualItems = itemTable.getChildCount();
        for (int i = 1; i < numOfIndividualItems; i++)
        {
            TableRow row = (TableRow) itemTable.getChildAt(i);
            String name = ((TextView) row.getChildAt(0)).getText().toString();
            double quantity = Double.valueOf(((TextView) row.getChildAt(1)).getText().toString());
            String type = ((TextView) row.getChildAt(2)).getText().toString();

            individualItems.add(new Ingredient(name, quantity, Type.getType(type)));
        }

        return individualItems;
    }
    private boolean listAlreadyExists(String listName)
    {
        Cursor resultSetList= database.rawQuery("Select * from ShoppingList WHERE name='"+listName+"';",null);
        resultSetList.moveToFirst();
        boolean listAlreadyExists = resultSetList.getCount() > 0;
        resultSetList.close();

        return listAlreadyExists;
    }

    private String getComment()
    {
        String comment = ((EditText) findViewById(R.id.comment)).getText().toString();
        if (comment.equals(""))
        {
            comment = "No comment entered";
        }

        return comment;
    }
    private void resetFields()
    {
        clearFields();

        selectRecGroup.clear();

        ViewCompat.setBackgroundTintList(findViewById(R.id.listName), ColorStateList.valueOf(Color.GRAY));
    }
    private void clearFields()
    {
        ((EditText) findViewById(R.id.listName)).setText("");

        LinearLayout recipeSelectedContainer = findViewById(R.id.selectRecipeContainer);
        for (int i = 1; i < recipeSelectedContainer.getChildCount(); i++)
        {
            recipeSelectedContainer.removeView(recipeSelectedContainer.getChildAt(i));
        }

        ((EditText) findViewById(R.id.itemNameText)).setText("");
        ((EditText) findViewById(R.id.quantityText)).setText("");
        ((Spinner) findViewById(R.id.typeSpinner)).setSelection(0);

        itemTable.removeViews(1, itemTable.getChildCount() - 1);
        itemTable.setVisibility(View.GONE);

        ((EditText) findViewById(R.id.comment)).setText("");
    }

    public void addRow(View view)
    {
        String itemName = ((EditText)findViewById(R.id.itemNameText)).getText().toString();
        String quantity = ((EditText)findViewById(R.id.quantityText)).getText().toString();
        String type = ((Spinner)findViewById(R.id.typeSpinner)).getSelectedItem().toString();

        if (!itemName.equals("") && itemName.matches("[ a-zA-Z]+") && !quantity.equals(""))
        {
            initTableRow(itemName, quantity, type);
        }else{
            Toast.makeText(this, "Invalid Entries", Toast.LENGTH_SHORT).show();
        }
    }
    private void initTableRow(String itemName, String quantity, String type)
    {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams fieldParam = new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f
        );

        quantity = String.valueOf(Double.valueOf(quantity));

        TextView textView = createTextView(itemName, fieldParam);
        TextView textView2 = createTextView(quantity, fieldParam);
        TextView textView3 = createTextView(type, fieldParam);


        TableRow.LayoutParams removeButtonParam = new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                .5f
        );
        Button removeButton = new Button(this);
        removeButton.setText("-");
        removeButton.setLayoutParams(removeButtonParam);
        removeButton.setGravity(Gravity.CENTER);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRow(view);
                if (itemTable.getChildCount() == 1)
                {
                    itemTable.setVisibility(View.GONE);
                }
            }
        });

        row.addView(textView);
        row.addView(textView2);
        row.addView(textView3);
        row.addView(removeButton);
        itemTable.addView(row);

        clearTextViews();

        itemTable.setVisibility(View.VISIBLE);
    }
    private TextView createTextView(String text, TableRow.LayoutParams fieldParam)
    {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(fieldParam);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
    private void clearTextViews()
    {
        ((EditText)findViewById(R.id.itemNameText)).setText("");
        ((EditText)findViewById(R.id.quantityText)).setText("");
        ((Spinner)findViewById(R.id.typeSpinner)).setSelection(0);
    }

    public void removeRow(View view)
    {
        itemTable.removeView((TableRow) view.getParent());
    }

    public void addRecipeComponent(View view)
    {

        if (spinnerArray.size() == 0)
        {
            Toast.makeText(this, "There are no available recipes to choose from", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectRecGroup.size() < spinnerArray.size())
        {
            List<String> temp;
            if (selectRecGroup.size() == 0)
            {
                temp = new ArrayList<>(spinnerArray);
            }else{
                temp = selectRecGroup.get(selectRecGroup.size()-1).getListWithoutSelectedItem();
            }

            final View views = LayoutInflater.from(this).inflate(R.layout.select_recipe, null);
            ((LinearLayout) findViewById(R.id.selectRecipeContainer)).addView(views);

            final SelectRecComponent selectRecComponent = new SelectRecComponent(
                    (Spinner) views.findViewById(R.id.spinner),
                    temp,
                    ((TextView) views.findViewById(R.id.seekBarValue)),
                    ((SeekBar) views.findViewById(R.id.seekBar)),
                    this, selectRecGroup);
            for (SelectRecComponent selectRecComponent_ : selectRecGroup)
            {
                if (!(views.findViewById(R.id.spinner)).equals(selectRecComponent_.getSpinner()))
                {
                    selectRecComponent_.removeItem(String.valueOf(((Spinner) views.findViewById(R.id.spinner)).getSelectedItem()));
                }
            }
        }else {
            Toast.makeText(this, "You have selected all the recipes", Toast.LENGTH_SHORT).show();
        }
    }
}
