package com.example.mycalc.Calculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.mycalc.Calculator.CalculatorHelpers.CalculationHelper;
import com.example.mycalc.Calculator.CalculatorHelpers.CalculationHistoryAdapter;
import com.example.mycalc.Calculator.CalculatorHelpers.HistoryVisibilityHandler;
import com.example.mycalc.CalculatorDatabase.DatabaseManager;
import com.example.mycalc.CalculatorDatabase.PreviousCalculation;
import com.example.mycalc.CalculatorDatabase.PreviousCalculationDatabase;
import com.example.mycalc.R;
import com.example.mycalc.UIDesignLogic.ButtonUtility;
import com.example.mycalc.UnitConverter.UnitConverter;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class BasicCalculatorActivity extends AppCompatActivity implements HistoryVisibilityHandler {

    private Button clearBtn, parenthesesBtn, percentsBtn, divisionBtn,
            numberSevenBtn, numberEightBtn, numberNineBtn, multiplicationBtn,
            numberFourBtn, numberFiveBtn, numberSixBtn, minusBtn, numberOneBtn,
            numberTwoBtn, numberThreeBtn, plusBtn, numberZeroBtn, dotBtn, equalBtn, deleteHistoryBtn, unknownOperator;
    private TextView currentCalculation, previousCalculation;
    private ListView historyTextViewField;
    private FrameLayout historyLayoutContainer;
    private ImageView backBtn, historyBtn, arithmeticCalculatorBtn, goToMeasureActivity;
    private boolean isHistoryVisible = false;
    private ConstraintLayout calculationButtons;
    private CalculationHistoryAdapter adapter;
    private PreviousCalculationDatabase previousCalculationDB;

    private ButtonUtility buttonUtility;
    private CalculationHelper calculationHelper;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calculationHelper = new CalculationHelper();
        buttonUtility = new ButtonUtility(this);
        databaseManager = new DatabaseManager(this);



        initViews();

        previousCalculationDB = Room.databaseBuilder(getApplicationContext(), PreviousCalculationDatabase.class, "PreviousCalculationDB").build();

        adapter = new CalculationHistoryAdapter(this, previousCalculationDB, this);
        historyTextViewField.setAdapter(adapter);

        adapter.setOnItemClickListener(calculation -> {
            calculationHelper.setContainer(calculation.getCalculation());
            previousCalculation.setText(calculation.getCalculation());
            currentCalculation.setText(calculation.getSum());
        });


        adapter.updateData();
        updateHistoryDeleteButtonVisibility();

        previousCalculation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ScrollView scv = findViewById(R.id.scrollView);
                scv.post(() -> scv.fullScroll(HorizontalScrollView.FOCUS_DOWN));
            }
        });

        setNumberButtonListeners();
        setOperationButtonListeners();
        setOtherButtonListeners();
    }

    private void initViews() {
        clearBtn = findViewById(R.id.clear_btn);
        parenthesesBtn = findViewById(R.id.parenthesis_btn);
        percentsBtn = findViewById(R.id.percentage_btn);
        divisionBtn = findViewById(R.id.division_btn);
        minusBtn = findViewById(R.id.minus_btn);
        multiplicationBtn = findViewById(R.id.multiply_btn);
        plusBtn = findViewById(R.id.plus_btn);
        dotBtn = findViewById(R.id.dot_btn);
        equalBtn = findViewById(R.id.equal_btn);
        numberZeroBtn = findViewById(R.id.zero_btn);
        numberOneBtn = findViewById(R.id.one_btn);
        numberTwoBtn = findViewById(R.id.two_btn);
        numberThreeBtn = findViewById(R.id.three_btn);
        numberFourBtn = findViewById(R.id.four_btn);
        numberFiveBtn = findViewById(R.id.five_btn);
        numberSixBtn = findViewById(R.id.six_btn);
        numberSevenBtn = findViewById(R.id.seven_btn);
        numberEightBtn = findViewById(R.id.eight_btn);
        numberNineBtn = findViewById(R.id.nine_btn);
        backBtn = findViewById(R.id.back_btn);
        historyTextViewField = findViewById(R.id.history_list_view);
        historyBtn = findViewById(R.id.clock_btn);
        calculationButtons = findViewById(R.id.main_btn_container);
        historyLayoutContainer = findViewById(R.id.history_list_container);
        deleteHistoryBtn = findViewById(R.id.delete_history);
        arithmeticCalculatorBtn = findViewById(R.id.arithmetic_calculator);
        currentCalculation = findViewById(R.id.equal_response);
        previousCalculation = findViewById(R.id.calculation_text_field);
        unknownOperator = findViewById(R.id.unknown_btn);
        goToMeasureActivity = findViewById(R.id.go_to_measurement);
    }

    private void setNumberButtonListeners() {

        buttonUtility.setButtonListenerWithResize(numberOneBtn, "1", () -> {
            handleNumberClick("1");
        }, 25f, 25f);

        buttonUtility.setButtonListenerWithResize(numberTwoBtn, "2", () -> {
            handleNumberClick("2");
        }, 20f, 25f);

        buttonUtility.setButtonListenerWithResize(numberThreeBtn, "3", () -> {
            handleNumberClick("3");
        }, 20f, 25f);

        buttonUtility.setButtonListenerWithResize(numberFourBtn, "4", () -> {
            handleNumberClick("4");
        }, 20f, 25f);

        buttonUtility.setButtonListenerWithResize(numberFiveBtn, "5", () -> {
            handleNumberClick("5");
        }, 20f, 25f);

        buttonUtility.setButtonListenerWithResize(numberSixBtn, "6", () -> {
            handleNumberClick("6");
        }, 20f, 25f);  // Correct placement of closing parentheses

        buttonUtility.setButtonListenerWithResize(numberSevenBtn, "7", () -> {
            handleNumberClick("7");
        }, 20f, 25f);  // Add size parameters

        buttonUtility.setButtonListenerWithResize(numberEightBtn, "8", () -> {
            handleNumberClick("8");
        }, 20f, 25f);  // Add size parameters

        buttonUtility.setButtonListenerWithResize(numberNineBtn, "9", () -> {
            handleNumberClick("9");
        }, 20f, 25f);  // Add size parameters

        buttonUtility.setButtonListenerWithResize(numberZeroBtn, "0", () -> {
            handleNumberClick("0");
        }, 20f, 25f);  // Add size parameters
    }



    private void handleNumberClick(String number) {
        calculationHelper.appendToContainerNumber(number);
        previousCalculation.setText(calculationHelper.getContainer());
        checkForResult();
    }


    private void setOperationButtonListeners() {
        clearBtn.setOnClickListener(v -> {
            calculationHelper.clearContainer();
            previousCalculation.setText(calculationHelper.getContainer());
            currentCalculation.setText("");
        });


        buttonUtility.setButtonListenerWithResize(divisionBtn, "/", () -> handleOperationClick("/"), 20f, 25f);
        buttonUtility.setButtonListenerWithResize(plusBtn, "+", () -> handleOperationClick("+"), 20f, 25f);
        buttonUtility.setButtonListenerWithResize(minusBtn, "-", () -> handleOperationClick("-"), 20f, 25f);
        buttonUtility.setButtonListenerWithResize(percentsBtn, "%", () -> handleOperationClick("%"), 20f, 25f);
        buttonUtility.setButtonListenerWithResize(dotBtn, ".", () -> handleOperationClick("."), 20f, 25f);
        buttonUtility.setButtonListenerWithResize(multiplicationBtn, "*", () -> handleOperationClick("*"), 20f, 25f);


        parenthesesBtn.setOnClickListener(v -> {
            String updatedContainer = calculationHelper.checkParentheses(calculationHelper.getContainer());
            calculationHelper.setContainer(updatedContainer);
            previousCalculation.setText(updatedContainer);
        });

        unknownOperator.setOnClickListener(v -> {
            String updatedContainer = calculationHelper.togglePlusMinus(calculationHelper.getContainer());
            calculationHelper.setContainer(updatedContainer);
            previousCalculation.setText(updatedContainer);
        });
}

    private void setOtherButtonListeners() {

        buttonUtility.setImageTouchListener(backBtn, this::handleBackButtonClick);
        buttonUtility.setImageTouchListener(historyBtn, this::toggleHistoryVisibility);
        buttonUtility.setImageTouchListener(arithmeticCalculatorBtn, () -> {
            Intent arithmeticCalculator = new Intent(BasicCalculatorActivity.this, ArithmeticCalculatorActivity.class);
            startActivity(arithmeticCalculator);
            overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
        });

        buttonUtility.setImageTouchListener(goToMeasureActivity, () -> {
            Intent measureUnitActivity = new Intent(BasicCalculatorActivity.this, UnitConverter.class);
            startActivity(measureUnitActivity);
        });

        deleteHistoryBtn.setOnClickListener(v -> clearHistory());

        equalBtn.setOnClickListener(v -> {
            if (currentCalculation.getText().toString().isEmpty()) {
                showErrorMessage();
                shakeTextView(previousCalculation);
            } else {
                String expressionStr = currentCalculation.getText().toString();
                PreviousCalculation calc1 = new PreviousCalculation(calculationHelper.getContainer(), expressionStr);
                databaseManager.addCalculation(calc1);
                calculationHelper.clearAllData(previousCalculation, currentCalculation); // Clear data after setting result
            }
        });
    }


    private void handleBackButtonClick() {
        String updatedContainer = calculationHelper.deleteLastCharacter(calculationHelper.getContainer());
        calculationHelper.setContainer(updatedContainer);
        previousCalculation.setText(updatedContainer);
        checkForResult();

    }

    private void handleOperationClick(String operation) {
        calculationHelper.appendToContainerOperation(operation);
        previousCalculation.setText(calculationHelper.getContainer());
        checkForResult();
    }


    private void clearHistory() {
         adapter.clearData();
         historyDeleteButtonVisibility();

    }

    public void historyDeleteButtonVisibility(){
        if(adapter.isEmpty()){
            deleteHistoryBtn.setVisibility(View.GONE);
        } else {
            deleteHistoryBtn.setVisibility(View.VISIBLE);
        }
    }


    private void toggleHistoryVisibility() {
        if (isHistoryVisible) {
            calculationButtons.setVisibility(View.VISIBLE);
            historyBtn.setImageResource(R.drawable.clock);
            historyLayoutContainer.setVisibility(View.GONE);
        } else {
            calculationButtons.setVisibility(View.GONE);
            historyBtn.setImageResource(R.drawable.smartphone_btn);
            historyLayoutContainer.setVisibility(View.VISIBLE);
            adapter.updateData();
        }
        isHistoryVisible = !isHistoryVisible;
    }

    @Override
    public void updateHistoryDeleteButtonVisibility() {
        if(adapter.isEmpty()){
            deleteHistoryBtn.setVisibility(View.GONE);
        } else {
            deleteHistoryBtn.setVisibility(View.VISIBLE);
        }
    }

    private void checkForResult() {
        if (!calculationHelper.checkLastChar()) {
            try {
                Expression expression = new ExpressionBuilder(calculationHelper.getContainer()).build();
                double result = expression.evaluate();
                if (Math.abs(result - Math.round(result)) < 1e-9) {
                    int finalResult = (int) Math.round(result);
                    currentCalculation.setText(String.valueOf(finalResult));
                } else {
                    currentCalculation.setText(String.format("%.2f", result));
                }
                Log.d("MainActivity", "Calculation added to database: " + " = " + currentCalculation.getText().toString());
            } catch (Exception e) {
                Log.e("CalcError", "Failed" + e);
                currentCalculation.setText("");
            }
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Calculation is incorrect, please check your input.", Toast.LENGTH_SHORT).show();
    }

    private void shakeTextView(TextView textView) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(50);
        shake.setInterpolator(new CycleInterpolator(7));
        textView.startAnimation(shake);
    }

}
