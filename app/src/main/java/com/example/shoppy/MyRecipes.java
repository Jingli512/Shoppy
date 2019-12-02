package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shoppy.Methods.ControlDB;
import java.util.List;

public class MyRecipes extends AppCompatActivity {
    TextView Recipe1Name;
    Button ViewRecipe;
    Button DeleteRecipe;
    TableLayout t1;
    TableRow tr;
    List<String> Lists;

    public static final String EXTRA_TITLE = "com.example.shoppy.extra.TITLE";

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        database = openOrCreateDatabase("ShoppyDB",MODE_PRIVATE,null);

        t1=findViewById(R.id.t1_msl);
        Lists = ControlDB.getRecipes(database);
        if (Lists.size() > 0) {
            findViewById(R.id.emptyRecipesLayout).setVisibility(View.GONE);
            t1.setColumnStretchable(0, true);
            t1.setColumnStretchable(1, true);
            t1.setColumnStretchable(2, true);
            t1.setColumnShrinkable(0, true);
            t1.setColumnShrinkable(1, true);
            t1.setColumnShrinkable(2, true);

            for (String s : Lists) {
                rowCreate(s);
            }
        }else{
            findViewById(R.id.emptyRecipesLayout).setVisibility(View.VISIBLE);
        }
    }

    public void  goToNewRecipe(View view){
        Intent intent = new Intent(this, NewRecipe.class);
        startActivity(intent);
    }

    public void  goToRecipeView(String n){
        Intent intent = new Intent(this, Recipe_View.class);
        intent.putExtra(EXTRA_TITLE, n);
        startActivity(intent);
    }

    public void removeRow(View v)
    {
        String s = ((TextView) ((TableRow) v.getParent()).getChildAt(0)).getText().toString();
        t1.removeView((TableRow) v.getParent());
        Lists.remove(s);

        ControlDB.removeRecipe(database, s);

        Toast.makeText(this, "Remove completed successfully", Toast.LENGTH_SHORT).show();

        if (Lists.size() == 0)
        {
            findViewById(R.id.emptyRecipesLayout).setVisibility(View.VISIBLE);
        }
    }

    public void rowCreate (String s)
    {
        final String NAME = s;
        tr = new TableRow(this);
        Recipe1Name = new TextView(this);
        ViewRecipe = new Button(this);
        DeleteRecipe = new Button (this);
        Recipe1Name.setText(s);
        Recipe1Name.setWidth(200);
        Recipe1Name.setTextSize(15);
        Recipe1Name.setPadding(16,16,16,16);

        ViewRecipe.setText("View");
        ViewRecipe.setTextSize(15);
        ViewRecipe.setWidth(100);
        ViewRecipe.setGravity(Gravity.CENTER);
        ViewRecipe.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                goToRecipeView(NAME);
            }
        });

        DeleteRecipe.setText("Delete");
        DeleteRecipe.setTextSize(15);
        DeleteRecipe.setWidth(100);
        DeleteRecipe.setGravity(Gravity.CENTER);
        DeleteRecipe.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                removeRow(v);
            }
        });

        tr.addView(Recipe1Name);
        tr.addView(ViewRecipe);
        tr.addView(DeleteRecipe);
        t1.addView(tr);

        findViewById(R.id.emptyRecipesLayout).setVisibility(View.GONE);
    }
}
