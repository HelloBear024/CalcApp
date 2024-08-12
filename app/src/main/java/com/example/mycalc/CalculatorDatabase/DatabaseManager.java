package com.example.mycalc.CalculatorDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

public class DatabaseManager {


    private PreviousCalculationDatabase db;

    public DatabaseManager(Context context) {
        db = Room.databaseBuilder(context, PreviousCalculationDatabase.class, "PreviousCalculationDB").build();
    }

    public void addCalculation(PreviousCalculation calculation) {
        new AddCalculationTask().execute(calculation);
    }

    // Inner class to handle the database operation asynchronously
    private class AddCalculationTask extends AsyncTask<PreviousCalculation, Void, Void> {

        @Override
        protected Void doInBackground(PreviousCalculation... calculations) {
            db.getPreviousCalculationDAO().addCalculation(calculations[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

}
