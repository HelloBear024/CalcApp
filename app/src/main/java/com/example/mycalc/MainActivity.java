package com.example.mycalc;

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

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
public class MainActivity extends AppCompatActivity implements HistoryVisibilityHandler {

    private Button clearBtn, parenthesesBtn, percentsBtn, divisionBtn,
            numberSevenBtn, numberEightBtn, numberNineBtn, multiplicationBtn,
            numberFourBtn, numberFiveBtn, numberSixBtn, minusBtn, numberOneBtn,
            numberTwoBtn, numberThreeBtn, plusBtn, numberZeroBtn, dotBtn, equalBtn, deleteHistoryBtn, unknownOperator;
    private TextView currentCalculation, previousCalculation;
    private ListView historyTextViewField;
    private FrameLayout historyLayoutContainer;
    private String container = "";
    private ImageView backBtn, historyBtn, arithmeticCalculatorBtn, goToMeasureActivity;
    private boolean isHistoryVisible = false;
    private ConstraintLayout calculationButtons;
    private CalculationHistoryAdapter adapter;
    private PreviousCalculationDatabase previousCalculationDB;
    Vibrator vibe;

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


        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initViews();


        previousCalculationDB = Room.databaseBuilder(getApplicationContext(), PreviousCalculationDatabase.class, "PreviousCalculationDB").build();

        adapter = new CalculationHistoryAdapter(this, previousCalculationDB, this);
        historyTextViewField.setAdapter(adapter);

        adapter.setOnItemClickListener(calculation -> {
            container = calculation.getCalculation();
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
        setButtonListenerWithResizeNumber(numberOneBtn, "1");
        setButtonListenerWithResizeNumber(numberTwoBtn, "2");
        setButtonListenerWithResizeNumber(numberThreeBtn, "3");
        setButtonListenerWithResizeNumber(numberFourBtn, "4");
        setButtonListenerWithResizeNumber(numberFiveBtn, "5");
        setButtonListenerWithResizeNumber(numberSixBtn, "6");
        setButtonListenerWithResizeNumber(numberSevenBtn, "7");
        setButtonListenerWithResizeNumber(numberEightBtn, "8");
        setButtonListenerWithResizeNumber(numberNineBtn, "9");
        setButtonListenerWithResizeNumber(numberZeroBtn, "0");
    }


    private void setOperationButtonListeners() {
        clearBtn.setOnClickListener(v -> {
            container = "";
            previousCalculation.setText(container);
            currentCalculation.setText("");
            setColorForBackBtn();
        });

        setButtonListenerWithResize(divisionBtn, "/");
        setButtonListenerWithResize(plusBtn, "+");
        setButtonListenerWithResize(minusBtn, "-");
        setButtonListenerWithResize(percentsBtn, "%");
        setButtonListenerWithResize(dotBtn, ".");
        setButtonListenerWithResize(multiplicationBtn, "*");

        parenthesesBtn.setOnClickListener(v -> {
            container = checkParentheses(container);
            previousCalculation.setText(container);
        });

        unknownOperator.setOnClickListener(v -> togglePlusMinus());
}

    private void setOtherButtonListeners() {

        setImageTouchListener(backBtn);
        setImageTouchListener(historyBtn);
        setImageTouchListener(arithmeticCalculatorBtn);
        setImageTouchListener(goToMeasureActivity);

        backBtn.setOnClickListener(v -> {
            container = deleteLastCharacter(container);
            previousCalculation.setText(container);
            checkForResult();
            setColorForBackBtn();

        });

        arithmeticCalculatorBtn.setOnClickListener(v -> {
            Intent arithmeticCalculator = new Intent(MainActivity.this, ArithmeticCalculatorActivity.class);
            startActivity(arithmeticCalculator);
            overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
        });

        goToMeasureActivity.setOnClickListener(v -> {
            Intent measureUnitActivity = new Intent( MainActivity.this, UnitConverter.class);
            startActivity(measureUnitActivity);

        });



        historyBtn.setOnClickListener(v -> toggleHistoryVisibility());

        deleteHistoryBtn.setOnClickListener(v -> {
                    clearHistory();
                }
        );

        equalBtn.setOnClickListener(v -> {
            if(currentCalculation.getText().toString().isEmpty()){
                showErrorMessage();
                shakeTextView(previousCalculation);
            }else{
            String expressionStr = currentCalculation.getText().toString();
                PreviousCalculation calc1 = new PreviousCalculation(container, expressionStr);
                new AddCalculationTask().execute(calc1);
                clearAllData();
            }
        });
    }




    public void setColorForBackBtn() {
        if (container.isEmpty()) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            backBtn.setColorFilter(filter);
            backBtn.setClickable(false);
        } else {
            backBtn.clearColorFilter();
            backBtn.setClickable(true);
        }
    }

    private void appendToContainerOperation(String value) {
        if (container.isEmpty() && (value.equals("+") || value.equals("-") || value.equals("/") || value.equals("*") || value.equals("%") || value.equals("."))) {
            return;
        }

        if (!container.isEmpty()) {
            char lastChar = container.charAt(container.length() - 1);
            if (isOperator(lastChar)) {
                container = container.substring(0, container.length() - 1);
            }
        }

        container += value;
        previousCalculation.setText(container);
        setColorForBackBtn();
        checkForResult();

    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '.';
    }



    private void setButtonListenerWithResize(Button button, String number) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setTextSize(25);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        button.setTextSize(30);
                        break;
                }
                return false;
            }
        });
        button.setOnClickListener(v -> {
            vibrateOnClick();
            appendToContainerOperation(number);
        });
    }

    private void setButtonListenerWithResizeNumber(Button button, String number) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setTextSize(25);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        button.setTextSize(30);
                        break;
                }
                return false;
            }
        });
        button.setOnClickListener(v -> {
            vibrateOnClick();
            appendToContainerNumber(number);
        });
    }

    private void appendToContainerNumber(String value) {
        container += value;
        previousCalculation.setText(container);
        setColorForBackBtn();
        checkForResult();
    }



    private void vibrateOnClick() {
        long[] pattern = {0, 10, 10, 10, 20};
        int[] amplitudes = {0, 70, 70, 60, 30};
        int repeatIdx = -1;

        if (vibe != null && vibe.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                vibe.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, repeatIdx));

            } else {
                vibe.vibrate(50);
            }
        } else {
            Log.e("ArithmeticCalculator", "Vibrator not available or not initialized");
        }
    }


    private void setImageTouchListener(ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imageView.setScaleX(0.9f);
                        imageView.setScaleY(0.9f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        imageView.setScaleX(1.0f);
                        imageView.setScaleY(1.0f);
                        break;
                }
                return false;
            }
        });
        imageView.setOnClickListener(v -> {
            vibrateOnClick();
        });
    }


    public String deleteLastCharacter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) != 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String checkParentheses(String str) {
        if (str == null) return null;
        int openCount = 0;
        int closeCount = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') openCount++;
            if (str.charAt(i) == ')') closeCount++;
        }
        if (openCount > closeCount) {
            if (str.length() > 0 && (Character.isDigit(str.charAt(str.length() - 1)) || str.charAt(str.length() - 1) == ')')) {
                str += ")";
            } else {
                str += "(";
            }
        } else {
            if (str.length() > 0 && (Character.isDigit(str.charAt(str.length() - 1)) || str.charAt(str.length() - 1) == ')')) {
                str += "*(";
            } else {
                str += "(";
            }
        }
        return str;
    }

    public void clearAllData() {
        if (currentCalculation.getText().toString().length() > 1) {
            previousCalculation.setText(currentCalculation.getText().toString());
            container = currentCalculation.getText().toString();
            currentCalculation.setText("");
        }
    }

    public boolean checkLastChar() {
        String str = currentCalculation.getText().toString();
        if (str.isEmpty()) return false;
        Set<Character> specialChars = new HashSet<>(Arrays.asList('(', '%', '÷', 'X', '-', '+'));
        return specialChars.contains(str.charAt(str.length() - 1));
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

    private class AddCalculationTask extends AsyncTask<PreviousCalculation, Void, Void> {
        @Override
        protected Void doInBackground(PreviousCalculation... calculations) {
            previousCalculationDB.getPreviousCalculationDAO().addCalculation(calculations[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("AddCalculationTask", "Calculation added, updating adapter data.");
            adapter.updateData();
            historyDeleteButtonVisibility();
        }
    }


    private void checkForResult() {
        if (!checkLastChar()) {
            try {
                Expression expression = new ExpressionBuilder(container).build();
                double result = expression.evaluate();
                if (Math.abs(result - Math.round(result)) < 1e-9) {
                    int finalResult = (int) Math.round(result);
                    currentCalculation.setText(String.valueOf(finalResult));
                } else {
                    currentCalculation.setText(String.format("%.2f", result));
                }
                Log.d("MainActivity", "Calculation added to database: " + container + " = " + currentCalculation.getText().toString());
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


    private void togglePlusMinus() {
        if (!container.isEmpty()) {
            int lastOperatorIndex = -1;
            for (int i = container.length() - 1; i >= 0; i--) {
                char c = container.charAt(i);
                if (c == '+' || c == '*' || c == '÷' || c == '%') {
                    lastOperatorIndex = i;
                    break;
                }
                // Handle special case for '-' as it might be a negative sign
                if (c == '-' && i > 0 && container.charAt(i - 1) != '(') {
                    lastOperatorIndex = i;
                    break;
                }
            }

            String number;
            String beforeNumber;
            if (lastOperatorIndex == -1) {
                number = container;
                beforeNumber = "";
            } else {
                // Operator found, work with the number after the operator
                number = container.substring(lastOperatorIndex + 1);
                beforeNumber = container.substring(0, lastOperatorIndex + 1);
            }

            if (!number.isEmpty()) {
                if (number.startsWith("(-")) {
                    // Remove the negative sign and parentheses if they exist
                    number = number.substring(2);
                } else if (number.startsWith("-")) {
                    // Handle standalone negative numbers
                    number = number.substring(1);
                } else {
                    // Add a negative sign and parentheses if it does not exist
                    number = "(-" + number;
                }
                container = beforeNumber + number;
                previousCalculation.setText(container);
            }
        }
    }
}
