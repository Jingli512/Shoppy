package com.example.shoppy.Methods;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectRecComponent
{
    private Spinner spinner;
    private SeekBar seekBar;
    private List<String> list;
    private String prev = "";

    public SelectRecComponent(final Spinner spinner,
                       final List<String> list,
                       final TextView seekBarValue,
                       final SeekBar seekBar,
                       final Context context,
                       final List<SelectRecComponent> group)
    {
        group.add(this);

        this.spinner = spinner;
        this.seekBar = seekBar;
        this.list = list;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (prev.equals("")){
                    prev = adapterView.getSelectedItem().toString();
                    setSeekBarValue(2);
                }else{
                    for (SelectRecComponent selectRecComponent : group)
                    {
                        if (!spinner.equals(selectRecComponent.spinner))
                        {
                            selectRecComponent.list.remove(adapterView.getSelectedItem().toString());
                            selectRecComponent.list.add(prev);
                        }
                    }
                    prev = String.valueOf(adapterView.getItemAtPosition(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                seekBarValue.setText(""+i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    public Spinner getSpinner()
    {
        return spinner;
    }

    public void removeItem(String item)
    {
        list.remove(item);
    }

    public List<String> getListWithoutSelectedItem()
    {
        List<String> result = new ArrayList<>(list);
        result.remove(spinner.getSelectedItemPosition());
        return result;
    }

    private void setSeekBarValue(int value)
    {
        seekBar.setProgress(value);
    }
}
