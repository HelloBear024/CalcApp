package com.example.mycalc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CalculationHistoryAdapter extends ArrayAdapter<Map.Entry<String, String>> {

    private Context context;
    private List<Map.Entry<String, String>> historyList;

    public CalculationHistoryAdapter(Context context, LinkedHashMap<String, String> historyArray) {
        super(context, 0, new ArrayList<>(historyArray.entrySet()));
        this.context = context;
        this.historyList = new ArrayList<>(historyArray.entrySet());    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.previous_calculation_cell, parent, false);
        }

        if (position < historyList.size()) {
            Map.Entry<String, String> entry = historyList.get(position);

            TextView cellCalculation = convertView.findViewById(R.id.calculation_text_field);
            TextView cellResult = convertView.findViewById(R.id.calculation_result);

            cellCalculation.setText(entry.getKey());
            cellResult.setText(entry.getValue());

            Log.d("Adapter", "called");

        }
        return convertView;

    }
    public void updateData(LinkedHashMap<String, String> newHistoryMap) {
        Log.d("Adapter", "Updating data");
        historyList.clear();
        historyList.addAll(newHistoryMap.entrySet());
        notifyDataSetChanged();
    }

    public void clearData() {
        Log.d("Adapter", "Clearing data");
        historyList.clear();
        notifyDataSetChanged();
    }

}
