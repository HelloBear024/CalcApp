package com.example.mycalc.CalculatorDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PreviousCalculation.class}, version = 1)
public abstract class PreviousCalculationDatabase extends RoomDatabase {

    public abstract PreviousCalculationDAO getPreviousCalculationDAO();


}
