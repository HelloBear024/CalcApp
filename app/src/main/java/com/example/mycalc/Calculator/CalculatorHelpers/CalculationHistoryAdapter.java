package com.example.mycalc.Calculator.CalculatorHelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mycalc.CalculatorDatabase.PreviousCalculation;
import com.example.mycalc.CalculatorDatabase.PreviousCalculationDatabase;
import com.example.mycalc.R;

import java.util.List;

public class CalculationHistoryAdapter extends ArrayAdapter<PreviousCalculation> {


    public interface OnItemClickListener {
        void onItemClick(PreviousCalculation calculation);
    }

    private HistoryVisibilityHandler visibilityHandler;
    private Context context;
    private List<PreviousCalculation> historyList;
    private PreviousCalculationDatabase db;
    private final LayoutInflater inflater;
    private  OnItemClickListener listener;

    public CalculationHistoryAdapter(Context context, PreviousCalculationDatabase db, HistoryVisibilityHandler visibilityHandler) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
        this.db = db;
        this.visibilityHandler = visibilityHandler;

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData() {
        new LoadDataTask().execute();
    }

    @Override
    public int getCount() {
        return historyList != null ? historyList.size() : 0;
    }

    @Override
    public PreviousCalculation getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.previous_calculation_cell, parent, false);
        }


        TextView cellCalculation = convertView.findViewById(R.id.calculation_text_field);
        TextView cellResult = convertView.findViewById(R.id.calculation_result);


        PreviousCalculation calculation = getItem(position);

        if (calculation != null ) {

            cellCalculation.setText(calculation.getCalculation());
            cellResult.setText(calculation.getSum());

            Log.d("Adapter getView", "getView called for position: " + position + ", calculation: " + calculation.getCalculation() + ", result: " + calculation.getSum());

        }else {
            Log.d("Adapter getView", "getView called for position: " + position + " but no data available");
        }

        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(calculation);
            }
        });


        return convertView;

    }

    private class LoadDataTask extends AsyncTask<Void, Void, List<PreviousCalculation>> {
        @Override
        protected List<PreviousCalculation> doInBackground(Void... voids) {
            return db.getPreviousCalculationDAO().getAllCalculations();
        }

        @Override
        protected void onPostExecute(List<PreviousCalculation> data) {
            historyList = data;
            notifyDataSetChanged();
            Log.d("CalculationHistoryAdapter", "Data loaded into adapter: " + historyList.size() + " records");
            visibilityHandler.updateHistoryDeleteButtonVisibility();
        }
    }


    public void clearData() {
        new ClearDataTask().execute();
    }

    private class ClearDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.getPreviousCalculationDAO().clearAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            historyList.clear();
            notifyDataSetChanged();
            Log.d("CalculationHistoryAdapter", "All data cleared from adapter and database");
            visibilityHandler.updateHistoryDeleteButtonVisibility();
        }
    }
}
