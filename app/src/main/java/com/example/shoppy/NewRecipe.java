package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppy.Methods.ControlDB;
import com.example.shoppy.Methods.Ingredient;
import com.example.shoppy.Methods.Recipe;
import com.example.shoppy.Methods.Type;

import java.util.ArrayList;

public class NewRecipe extends AppCompatActivity {

    private TableLayout table;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        table = findViewById(R.id.itemTable);
        database = openOrCreateDatabase("ShoppyDB",MODE_PRIVATE,null);

        initSeekBar();
    }

    public void createNewRecipe(View view)
    {
        String recName = ((EditText) findViewById(R.id.recName)).getText().toString();
        int people = ((SeekBar) findViewById(R.id.seekBarPeople)).getProgress();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String method = getMethod();

        if (!recName.equals(""))
        {
            if (!recipeExists(recName))
            {
                if (table.getChildCount() != 1)
                {
                    for (int i = 1; i < table.getChildCount(); i++)
                    {
                        TableRow row = (TableRow) table.getChildAt(i);
                        String name = ((TextView) row.getChildAt(0)).getText().toString();
                        double quantity = Double.valueOf(((TextView) row.getChildAt(1)).getText().toString());
                        Type type = Type.getType(((TextView) row.getChildAt(2)).getText().toString());

                        ingredients.add(new Ingredient(name, quantity, type));
                    }

                    clearFields();

                    Recipe recipe = new Recipe(recName, ingredients, people, method);
                    ControlDB.addRecipesToDB(this, database, recipe);
                    Toast.makeText(this, "Recipe created Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "There are no ingredients made for the recipe", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "There already exists a Recipe with the same name", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Enter the name of the Recipe", Toast.LENGTH_LONG).show();
        }
    }
    private boolean recipeExists(String name)
    {
        Cursor resultSetRec= database.rawQuery("Select * from Recipe WHERE Name='"+name+"';",null);
        resultSetRec.moveToFirst();
        boolean listAlreadyExists = resultSetRec.getCount() > 0;
        resultSetRec.close();

        return listAlreadyExists;
    }
    private String getMethod()
    {
        String method = ((EditText) findViewById(R.id.recMethod)).getText().toString();
        if (method.equals(""))
        {
            method = "No comment entered";
        }

        return method;
    }

    private void clearFields()
    {
        ((EditText) findViewById(R.id.recName)).setText("");
        ((SeekBar) findViewById(R.id.seekBarPeople)).setProgress(4);

        ((EditText) findViewById(R.id.itemNameText)).setText("");
        ((EditText) findViewById(R.id.quantityText)).setText("");
        ((Spinner) findViewById(R.id.typeSpinner)).setSelection(0);

        table.removeViews(1, table.getChildCount() - 1);
        table.setVisibility(View.GONE);

        ((EditText) findViewById(R.id.recMethod)).setText("");
    }

    private void initSeekBar()
    {
        ((SeekBar) findViewById(R.id.seekBarPeople)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                ((TextView)findViewById(R.id.seekBarValuePeople)).setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
        ((SeekBar) findViewById(R.id.seekBarPeople)).setProgress(4);
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
                if (table.getChildCount() == 2)
                {
                    Toast.makeText(NewRecipe.this, "You must have at least one ingredient to your recipe", Toast.LENGTH_SHORT).show();
                }else{
                    removeRow(view);
                }
            }
        });

        row.addView(textView);
        row.addView(textView2);
        row.addView(textView3);
        row.addView(removeButton);
        table.addView(row);

        clearTextViews();

        table.setVisibility(View.VISIBLE);
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
        table.removeView((TableRow) view.getParent());
    }
}