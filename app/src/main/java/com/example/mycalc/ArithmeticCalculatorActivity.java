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
import net.objecthunter.exp4j.function.Function;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArithmeticCalculatorActivity extends AppCompatActivity implements HistoryVisibilityHandler{

        private ImageView backBtn, historyBtn, basicCalculatorBtn;

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

            initViews();

            previousCalculationDB = Room.databaseBuilder(getApplicationContext(), PreviousCalculationDatabase.class, "PreviousCalculationDB").build();
            Log.d("ArithmeticCalculator", "Database initialized");

            adapter = new CalculationHistoryAdapter(this, previousCalculationDB, this);
            historyTextViewField.setAdapter(adapter);
            adapter.updateData();

            basicCalculatorBtn.setOnClickListener(v -> {
                Intent basicCalculator = new Intent(ArithmeticCalculatorActivity.this, MainActivity.class);
                ArithmeticCalculatorActivity.this.startActivity(basicCalculator);
            });

            adapter.setOnItemClickListener(calculation -> {
                container = calculation.getCalculation();
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


            regDegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isDegClicked) {
                        regAndDegTextView.setVisibility(View.VISIBLE);
                        isDegClicked = true;
                    } else {
                        regAndDegTextView.setVisibility(View.INVISIBLE);
                        isDegClicked = false;
                    }
                }
            });
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
            setButtonListenerWithResize(precentsBtn, "%");
            setButtonListenerWithResize(dotBtn, ".");
            setButtonListenerWithResize(multiplicationBtn, "*");

            parenthesesBtn.setOnClickListener(v -> {
                checkForResult();
                container = checkParentheses(container);
                previousCalculation.setText(container);
                checkForResult();
            });

            plusMinusBtn.setOnClickListener(v -> togglePlusMinus());
        }

        private void setOtherButtonListeners() {
            setImageTouchListener(backBtn);
            setImageTouchListener(historyBtn);
            setImageTouchListener(basicCalculatorBtn);

            backBtn.setOnClickListener(v -> {

                container = deleteLastCharacter(container);
                previousCalculation.setText(container);
                checkForResult();
            });

            basicCalculatorBtn.setOnClickListener(v -> {
                Intent basicCalculator = new Intent(ArithmeticCalculatorActivity.this, MainActivity.class);
                startActivity(basicCalculator);
                overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in);
            });

            historyBtn.setOnClickListener(v -> toggleHistoryVisibility());

            deleteHistoryBtn.setOnClickListener(v -> clearHistory());

            equalBtn.setOnClickListener(v -> {
                if(currentCalculation.getText().toString().isEmpty()){
                    showErrorMessage();
                    shakeTextView(previousCalculation);
                }else{
                    checkForResult();
                    String expressionStr = currentCalculation.getText().toString();
                    PreviousCalculation calc1 = new PreviousCalculation(container, expressionStr);
                    new AddCalculationTask().execute(calc1);
                    clearAllData();
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

    private void appendToContainer(String value) {
        container += value;
        previousCalculation.setText(container);
        setColorForBackBtn();
        checkForResult();
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
        if (!checkLastChar()) {
            try {
                String expressionToEvaluate = container;
                if (isDegClicked) {
                    expressionToEvaluate = convertToRadians(container);
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
                Log.d("MainActivity", "Calculation added to database: " + container + " = " + currentCalculation.getText().toString());
            } catch (Exception e) {
                Log.e("CalcError", "Failed" + e);
                currentCalculation.setText("");
            }
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
                    checkForResult();
                } else {
                    str += "(";
                    checkForResult();
                }
            } else {
                if (str.length() > 0 && (Character.isDigit(str.charAt(str.length() - 1)) || str.charAt(str.length() - 1) == ')')) {
                    str += "*(";
                    checkForResult();
                } else {
                    str += "(";
                    checkForResult();
                }
            }
            checkForResult();
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

    private void setButtonListenerWithResizeNumber(Button button, String number) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setTextSize(15);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        button.setTextSize(20);
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


    private void setButtonListenerWithResize(Button button, String number) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setTextSize(15);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        button.setTextSize(20);
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
        if (!container.isEmpty()) {
            char lastChar = container.charAt(container.length() - 1);
            // Special handling for ^3
            if (function.equals("^(3)")) {
                if (Character.isDigit(lastChar)) {
                    container += function;
                    previousCalculation.setText(container);
                    setColorForBackBtn();
                    checkForResult();
                    return;
                } else if (isOperator(lastChar)) {
                    container = container.substring(0, container.length() - 1) + function;
                    previousCalculation.setText(container);
                    setColorForBackBtn();
                    checkForResult();
                    return;
                } else {
                    return;
                }
            }

            if (Character.isDigit(lastChar) || lastChar == ')') {
                container += "*";
            }
        } else {
            if (function.equals("!") || function.equals("^(3)")) {
                return;
            }
        }

        // Append the function and update the display
        container += function;
        previousCalculation.setText(container);
        setColorForBackBtn();
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


    private class AddCalculationTask extends AsyncTask<PreviousCalculation, Void, Void> {
            @Override
            protected Void doInBackground(PreviousCalculation... calculations) {
                previousCalculationDB.getPreviousCalculationDAO().addCalculation(calculations[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.updateData();
            }
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
                number = container.substring(lastOperatorIndex + 1);
                beforeNumber = container.substring(0, lastOperatorIndex + 1);
            }

            if (!number.isEmpty()) {
                if (number.startsWith("(-")) {
                    number = number.substring(2);
                } else if (number.startsWith("-")) {
                    number = number.substring(1);
                } else {
                    number = "(-" + number;
                }
                container = beforeNumber + number;
                previousCalculation.setText(container);
            }
        }
    }
}




