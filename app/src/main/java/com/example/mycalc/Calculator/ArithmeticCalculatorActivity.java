package com.example.mycalc.Calculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
import com.example.mycalc.Calculator.CalculatorHelpers.CustomFunctionsForArithmetic;
import com.example.mycalc.Calculator.CalculatorHelpers.HistoryVisibilityHandler;
import com.example.mycalc.CalculatorDatabase.DatabaseManager;
import com.example.mycalc.CalculatorDatabase.PreviousCalculation;
import com.example.mycalc.CalculatorDatabase.PreviousCalculationDatabase;
import com.example.mycalc.R;
import com.example.mycalc.UIDesignLogic.ButtonUtility;
import com.example.mycalc.UnitConverter.UnitConverter;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArithmeticCalculatorActivity extends AppCompatActivity implements HistoryVisibilityHandler {

        private ImageView backBtn, historyBtn, basicCalculatorBtn, goToMeasureActivity;

        private Button clearBtn, parenthesesBtn, precentsBtn, divisionBtn,
                numberSevenBtn, numberEightBtn, numberNineBtn, multiplicationBtn,
                numberFourBtn, numberFiveBtn, numberSixBtn, minusBtn, numberOneBtn,
                numberTwoBtn, numberThreeBtn, plusBtn, numberZeroBtn, dotBtn, equalBtn, deleteHistoryBtn, plusMinusBtn;

        private Button inverseButtonsBtn, regDegBtn, squareRootBtn, sinusBtn, cosBtn, tangentBtn,
                logarithmFunctionBtn, logarithmBtn, reciprocal_function_btn,
                exponentialFunctionBtn, toSquareBtn, exponentiationBtn,
                absoluteValueBtn, piBtn, eulerBtn;
        private String container = "";
        private boolean isHistoryVisible = false;
        private boolean isInvertedButtonPressed = true;
        private ConstraintLayout calculationButtons, historyLayout;
        private TextView currentCalculation, previousCalculation, regAndDegTextView;
        private ListView historyTextViewField;
        private PreviousCalculationDatabase previousCalculationDB;
        private CalculationHistoryAdapter adapter;
        Vibrator vibe;
        boolean isDegClicked = false;
        private ButtonUtility buttonUtility;
        private CalculationHelper calculationHelper;
        private DatabaseManager databaseManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_arithmetic_calculator);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            if (vibe == null || !vibe.hasVibrator()) {
                Log.e("ArithmeticCalculator", "Vibrator not available");
            }

            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            buttonUtility = new ButtonUtility(this);
            calculationHelper = new CalculationHelper();
            databaseManager = new DatabaseManager(this);

            initViews();

            previousCalculationDB = Room.databaseBuilder(getApplicationContext(), PreviousCalculationDatabase.class, "PreviousCalculationDB").build();
            Log.d("ArithmeticCalculator", "Database initialized");

            adapter = new CalculationHistoryAdapter(this, previousCalculationDB, this);
            historyTextViewField.setAdapter(adapter);
            adapter.updateData();

            adapter.setOnItemClickListener(calculation -> {
                calculationHelper.setContainer(calculation.getCalculation());
                previousCalculation.setText(calculation.getCalculation());
                currentCalculation.setText(calculation.getSum());
            });

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
                    scv.post(() -> scv.fullScroll(ScrollView.FOCUS_DOWN));
                }
            });

            setNumberButtonListeners();
            setOperationButtonListeners();
            setOtherButtonListeners();
        }

        private void initViews() {
            clearBtn = findViewById(R.id.clear_btn);
            parenthesesBtn = findViewById(R.id.parenthesis_btn);
            precentsBtn = findViewById(R.id.percentage_btn);
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
            basicCalculatorBtn = findViewById(R.id.basic_calculator_btn);
            deleteHistoryBtn = findViewById(R.id.delete_history);
            currentCalculation = findViewById(R.id.equal_response);
            previousCalculation = findViewById(R.id.calculation_text_field);
            inverseButtonsBtn = findViewById(R.id.inverse_buttons);
            regDegBtn = findViewById(R.id.rad_and_deg_btn);
            squareRootBtn = findViewById(R.id.square_root_btn);
            sinusBtn = findViewById(R.id.sinus_btn);
            cosBtn = findViewById(R.id.cos_btn);
            tangentBtn = findViewById(R.id.tangent_function_btn);
            logarithmFunctionBtn = findViewById(R.id.logarithm_function_btn);
            logarithmBtn = findViewById(R.id.logarithm_btn);
            reciprocal_function_btn = findViewById(R.id.reciprocal_function_btn);
            exponentialFunctionBtn = findViewById(R.id.exponential_function_btn);
            toSquareBtn = findViewById(R.id.square_btn);
            exponentiationBtn = findViewById(R.id.exponentiation_btn);
            absoluteValueBtn = findViewById(R.id.absolute_value_btn);
            piBtn = findViewById(R.id.pi_btn);
            eulerBtn = findViewById(R.id.euler_btn);
            historyLayout = findViewById(R.id.history_list_container_layout);
            calculationButtons = findViewById(R.id.main_btn_container);
            regAndDegTextView = findViewById(R.id.reg_and_deg_text_view);
            regDegBtn = findViewById(R.id.rad_and_deg_btn);
            plusMinusBtn = findViewById(R.id.unknown_btn);
            goToMeasureActivity = findViewById(R.id.go_to_measurement);


            regDegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isDegClicked) {
                        regAndDegTextView.setVisibility(View.VISIBLE);
                        isDegClicked = true;
                        checkForResult();
                    } else {
                        regAndDegTextView.setVisibility(View.INVISIBLE);
                        isDegClicked = false;
                        checkForResult();
                    }
                }
            });
        }


    private void setNumberButtonListeners() {

        buttonUtility.setButtonListenerWithResize(numberOneBtn, "1", () -> handleNumberClick("1"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberTwoBtn, "2", () -> handleNumberClick("2"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberThreeBtn, "3", () -> handleNumberClick("3"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberFourBtn, "4", () -> handleNumberClick("4"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberFiveBtn, "5", () -> handleNumberClick("5"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberSixBtn, "6", () -> handleNumberClick("6"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberSevenBtn, "7", () -> handleNumberClick("7"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberEightBtn, "8", () -> handleNumberClick("8"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberNineBtn, "9", () -> handleNumberClick("9"), 15f, 20f);
        buttonUtility.setButtonListenerWithResize(numberZeroBtn, "0", () -> handleNumberClick("0"), 15f, 20f);

    }



        private void setOperationButtonListeners() {
            clearBtn.setOnClickListener(v -> {
                calculationHelper.clearContainer();
                previousCalculation.setText(calculationHelper.getContainer());
                currentCalculation.setText("");
            });

            buttonUtility.setButtonListenerWithResize(divisionBtn, "/", () -> handleOperationClick("/"), 15f, 20f);
            buttonUtility.setButtonListenerWithResize(plusBtn, "+", () -> handleOperationClick("+"), 15f, 20f);
            buttonUtility.setButtonListenerWithResize(minusBtn, "-", () -> handleOperationClick("-"), 15f, 20f);
            buttonUtility.setButtonListenerWithResize(precentsBtn, "%", () -> handleOperationClick("%"), 15f, 20f);
            buttonUtility.setButtonListenerWithResize(dotBtn, ".", () -> handleOperationClick("."), 15f, 20f);
            buttonUtility.setButtonListenerWithResize(multiplicationBtn, "*", () -> handleOperationClick("*"), 15f, 20f);


            parenthesesBtn.setOnClickListener(v -> {
                String updatedContainer = calculationHelper.checkParentheses(calculationHelper.getContainer());
                calculationHelper.setContainer(updatedContainer);
                previousCalculation.setText(updatedContainer);
                checkForResult();
            });

            plusMinusBtn.setOnClickListener(v -> {
                String updatedContainer = calculationHelper.togglePlusMinus(calculationHelper.getContainer());
                calculationHelper.setContainer(updatedContainer);
                previousCalculation.setText(updatedContainer);
            });
        }

        private void setOtherButtonListeners() {
            buttonUtility.setImageTouchListener(backBtn, this::handleBackButtonClick);
            buttonUtility.setImageTouchListener(historyBtn, this::toggleHistoryVisibility);
            buttonUtility.setImageTouchListener(basicCalculatorBtn, () -> {
                Intent basicCalculator = new Intent(ArithmeticCalculatorActivity.this, BasicCalculatorActivity.class);
                startActivity(basicCalculator);
                overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
            });
            buttonUtility.setImageTouchListener(goToMeasureActivity, () -> {
                Intent measureUnitActivity = new Intent(ArithmeticCalculatorActivity.this, UnitConverter.class);
                startActivity(measureUnitActivity);
            });



            basicCalculatorBtn.setOnClickListener(v -> {
                Intent basicCalculator = new Intent(ArithmeticCalculatorActivity.this, BasicCalculatorActivity.class);
                startActivity(basicCalculator);
                overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
            });

            historyBtn.setOnClickListener(v -> toggleHistoryVisibility());

            deleteHistoryBtn.setOnClickListener(v -> clearHistory());

            equalBtn.setOnClickListener(v -> {
                if (currentCalculation.getText().toString().isEmpty()) {
                    showErrorMessage();
                    shakeTextView(previousCalculation);
                } else {
                    String expressionStr = currentCalculation.getText().toString();
                    PreviousCalculation calc1 = new PreviousCalculation(calculationHelper.getContainer(), expressionStr);
                    databaseManager.addCalculation(calc1);
                    calculationHelper.clearAllData(previousCalculation, currentCalculation);
                }
            });

            inverseButtonsBtn.setOnClickListener(v -> switchArithmeticButtonsProperty());
            squareRootBtn.setOnClickListener(v -> appendFunction(squareRootBtn.getText().toString().equals("√") ? "sqrt(" : "cbrt("));
            sinusBtn.setOnClickListener(v -> appendFunction(sinusBtn.getText().toString().equals("sin") ? "sin(" : "asin("));
            cosBtn.setOnClickListener(v -> appendFunction(cosBtn.getText().toString().equals("cos") ? "cos(" : "acos("));
            tangentBtn.setOnClickListener(v -> appendFunction(tangentBtn.getText().toString().equals("tan") ? "tan(" : "atan("));
            logarithmFunctionBtn.setOnClickListener(v -> appendFunction(logarithmFunctionBtn.getText().toString().equals("In") ? "In(" : "sinh("));
            logarithmBtn.setOnClickListener(v -> appendFunction(logarithmBtn.getText().toString().equals("log") ? "log(" : "cosh("));
            reciprocal_function_btn.setOnClickListener(v -> appendFunction(reciprocal_function_btn.getText().toString().equals("1/x") ? "1÷" : "tanh("));
            exponentialFunctionBtn.setOnClickListener(v -> appendFunction(exponentialFunctionBtn.getText().toString().equals("eˣ") ? "e^(" : "asinh("));
            toSquareBtn.setOnClickListener(v -> appendFunction(toSquareBtn.getText().toString().equals("x²") ? "^(2)" : "acosh("));
            exponentiationBtn.setOnClickListener(v -> appendFunction(exponentiationBtn.getText().toString().equals("xʸ") ? "^(" : "atanh("));
            absoluteValueBtn.setOnClickListener(v -> appendFunction(absoluteValueBtn.getText().toString().equals("|x|") ? "abs(" : "2^("));
            piBtn.setOnClickListener(v -> appendFunction(piBtn.getText().toString().equals("π") ? "3.1415926536" : "^(3)"));
            eulerBtn.setOnClickListener(v -> appendFunction(eulerBtn.getText().toString().equals("e") ? "e" : "fact("));
        }

    private void handleNumberClick(String number) {
        calculationHelper.appendToContainerNumber(number);
        previousCalculation.setText(calculationHelper.getContainer());
        checkForResult();
    }

    private void handleOperationClick(String operation) {
        calculationHelper.appendToContainerOperation(operation);
        previousCalculation.setText(calculationHelper.getContainer());
        checkForResult();
    }

    private void handleBackButtonClick() {
        String updatedContainer = calculationHelper.deleteLastCharacter(calculationHelper.getContainer());
        calculationHelper.setContainer(updatedContainer);
        previousCalculation.setText(updatedContainer);
        checkForResult();
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

    private void checkForResult() {
        if (!calculationHelper.checkLastChar()) {
            try {
                String expressionToEvaluate = calculationHelper.getContainer();
                if (isDegClicked) {
                    expressionToEvaluate = convertToRadians(expressionToEvaluate);
                }
                List<Function> customFunctions = CustomFunctionsForArithmetic.getFunctions();
                Expression expression = new ExpressionBuilder(expressionToEvaluate).functions(customFunctions).build();
                double result = expression.evaluate();
                if (Math.abs(result - Math.round(result)) < 1e-9) {
                    int finalResult = (int) Math.round(result);
                    currentCalculation.setText(String.valueOf(finalResult));
                } else {
                    currentCalculation.setText(String.format("%.8f", result));
                }
                Log.d("ArithmeticCalculator", "Calculation added to database: " + calculationHelper.getContainer() + " = " + currentCalculation.getText().toString());
            } catch (Exception e) {
                Log.e("CalcError", "Failed" + e);
                currentCalculation.setText("");
            }
        }
    }

        private void clearHistory() {
            Log.d("ArithmeticCalculator", "Clearing history");
            adapter.clearData();
        }

        private void toggleHistoryVisibility() {
            if (isHistoryVisible) {
                calculationButtons.setVisibility(View.VISIBLE);
                historyBtn.setImageResource(R.drawable.clock);
                historyLayout.setVisibility(View.GONE);
            } else {
                calculationButtons.setVisibility(View.GONE);
                historyBtn.setImageResource(R.drawable.smartphone_btn);
                historyLayout.setVisibility(View.VISIBLE);
                adapter.updateData();
            }
            isHistoryVisible = !isHistoryVisible;
        }


        private void switchArithmeticButtonsProperty() {
            if (isInvertedButtonPressed) {
                squareRootBtn.setText("∛");
                sinusBtn.setText("sin⁻¹");
                cosBtn.setText("cos⁻¹");
                tangentBtn.setText("tan⁻¹");
                logarithmFunctionBtn.setText("sinh");
                logarithmBtn.setText("cosh");
                reciprocal_function_btn.setText("tanh");
                exponentialFunctionBtn.setText("sinh⁻¹");
                toSquareBtn.setText("cosh⁻¹");
                exponentiationBtn.setText("tanh⁻¹");
                absoluteValueBtn.setText("2ˣ");
                piBtn.setText("x³");
                eulerBtn.setText("x!");
                isInvertedButtonPressed = false;
            } else {
                squareRootBtn.setText(R.string.square_root_btn);
                sinusBtn.setText(R.string.sinus_btn);
                cosBtn.setText(R.string.cos_btn);
                tangentBtn.setText(R.string.tangent_function_btn);
                logarithmFunctionBtn.setText(R.string.logarithm_function_btn);
                logarithmBtn.setText(R.string.logarithm_btn);
                reciprocal_function_btn.setText(R.string.reciprocal_function_btn);
                exponentialFunctionBtn.setText(R.string.exponential_function_btn);
                toSquareBtn.setText(R.string.square_btn);
                exponentiationBtn.setText(R.string.exponentiation_btn);
                absoluteValueBtn.setText(R.string.absolute_value_btn);
                piBtn.setText(R.string.pi_btn);
                eulerBtn.setText(R.string.euler_btn);
                isInvertedButtonPressed = true;
            }
        }

    private void appendFunction(String function) {
        calculationHelper.appendFunction(function);
        previousCalculation.setText(calculationHelper.getContainer());
        checkForResult();
    }


    private String convertToRadians(String expression) {
        return expression.replaceAll("sin\\((.*?)\\)", "sin($1 * pi / 180)")
                .replaceAll("cos\\((.*?)\\)", "cos($1 * pi / 180)")
                .replaceAll("tan\\((.*?)\\)", "tan($1 * pi / 180)");
    }

    @Override
    public void updateHistoryDeleteButtonVisibility() {
        if(adapter.isEmpty()){
            deleteHistoryBtn.setVisibility(View.GONE);
        } else {
            deleteHistoryBtn.setVisibility(View.VISIBLE);
        }
    }

}




