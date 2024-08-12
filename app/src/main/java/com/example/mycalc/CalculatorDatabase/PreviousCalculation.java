package com.example.mycalc.CalculatorDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "PreviousCalculation")
public class PreviousCalculation {

    //private LinkedHashMap<String, String> historyArray = new LinkedHashMap<>();
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="previous_calculation_id")
    private int id;

    @ColumnInfo(name = "calculation")
    private String calculation;

    @ColumnInfo(name = "sum")
    private String sum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Ignore
    public PreviousCalculation(){};

    public PreviousCalculation(String calculation, String sum) {
        this.id = 0;
        this.calculation = calculation;
        this.sum = sum;
    };

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCalculation() {
        return calculation;
    }

    public String getSum() {
        return sum;
    }
}
