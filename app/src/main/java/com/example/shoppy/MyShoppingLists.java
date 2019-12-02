package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppy.Methods.ControlDB;

import java.util.List;

public class MyShoppingLists extends AppCompatActivity {
    TextView List1Name;
    Button ViewShoppingList;
    Button DeleteShoppingList;
    TableLayout t1;
    TableRow tr;
    List<String> Lists;

    public static final String EXTRA_TITLE = "com.example.shoppy.extra.TITLE";

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping_lists);

        database = openOrCreateDatabase("ShoppyDB",MODE_PRIVATE,null);

        t1=findViewById(R.id.t1_msl);
        Lists = ControlDB.getShoppingLists(database);
        if (Lists.size() > 0)
        {
            findViewById(R.id.emptyRecipesLayout).setVisibility(View.GONE);
            t1.setColumnStretchable(0,true);
            t1.setColumnStretchable(1,true);
            t1.setColumnStretchable(2,true);
            t1.setColumnShrinkable(0,true);
            t1.setColumnShrinkable(1,true);
            t1.setColumnShrinkable(2,true);

            for (String s : Lists)
            {
                rowCreate(s);
            }
        }else{
            findViewById(R.id.emptyRecipesLayout).setVisibility(View.VISIBLE);
        }
    }

    public void  goToNewShoppingList(View view){
        Intent intent = new Intent(this, NewShoppingList.class);
        startActivity(intent);
    }

    public void  goToListView(String n){
        Intent intent = new Intent(this, ShoppingListView.class);
        intent.putExtra(EXTRA_TITLE, n);
        startActivity(intent);
    }

    public void removeRow(View v)
    {
        String s = ((TextView) ((TableRow) v.getParent()).getChildAt(0)).getText().toString();
        t1.removeView((TableRow) v.getParent());
        Lists.remove(s);

        ControlDB.removeShoppingList(database, s);

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
        List1Name = new TextView(this);
        ViewShoppingList = new Button(this);
        DeleteShoppingList = new Button (this);
        List1Name.setText(s);
        List1Name.setWidth(200);
        List1Name.setTextSize(15);
        List1Name.setPadding(16,16,16,16);

        ViewShoppingList.setText("View");
        ViewShoppingList.setTextSize(15);
        ViewShoppingList.setWidth(100);
        ViewShoppingList.setGravity(Gravity.CENTER);
        ViewShoppingList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                goToListView(NAME);
            }
        });

        DeleteShoppingList.setText("Delete");
        DeleteShoppingList.setTextSize(15);
        DeleteShoppingList.setWidth(100);
        DeleteShoppingList.setGravity(Gravity.CENTER);
        DeleteShoppingList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                removeRow(v);
            }
        });

        tr.addView(List1Name);
        tr.addView(ViewShoppingList);
        tr.addView(DeleteShoppingList);
        t1.addView(tr);

        findViewById(R.id.emptyRecipesLayout).setVisibility(View.GONE);
    }
}
