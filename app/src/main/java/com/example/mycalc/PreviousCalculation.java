package com.example.mycalc;

import java.util.LinkedHashMap;

public class PreviousCalculation {

    private LinkedHashMap<String, String> historyArray = new LinkedHashMap<>();

    private String calculation;

    private String sum;

    public PreviousCalculation(){};

    public PreviousCalculation(String calculation, String sum) {
        this.calculation = calculation;
        this.sum = sum;
        addCalculationToHistory(calculation, sum);
    };

    public LinkedHashMap<String, String> getHistoryArray() {
        return historyArray;
    }

    public void setHistoryArray(LinkedHashMap<String, String> historyArray) {
        this.historyArray = historyArray;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
        addCalculationToHistory(calculation, this.sum);
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    void addCalculationToHistory(String calculation, String sum) {
        if (calculation != null && sum != null) {
            historyArray.put(calculation, sum);
        }
    }



    public String getCalculation() {
        return calculation;
    }

    public String getSum() {
        return sum;
    }
}
