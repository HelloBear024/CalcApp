package com.example.mycalc.CalculatorDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PreviousCalculationDAO {

    @Insert
    public void addCalculation(PreviousCalculation previousCalculation);

    @Update
    public void updateCalculation(PreviousCalculation previousCalculation);

    @Delete
    public void deleteCalculation(PreviousCalculation previousCalculation);

    @Query("DELETE FROM PreviousCalculation")
    void clearAll();

    @Query("select * from PreviousCalculation ")
    public List<PreviousCalculation> getAllCalculations();

    @Query("select * from PreviousCalculation where previous_calculation_id==:previous_calculation_id")
    public PreviousCalculation getPreviousCalculation(int previous_calculation_id);

}
