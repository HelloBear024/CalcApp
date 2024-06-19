package com.example.mycalc;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private Button clearBtn, parenthesesBtn, precentsBtn, divisionBtn,
            numberSevenBtn, numberEightBtn, numberNineBtn, multiplicationBtn,
            numberFourBtn, numberFiveBtn, numberSixBtn, minusBtn, numberOneBtn,
            numberTwoBtn, numberThreeBtn, plusBtn, numberZeroBtn, dotBtn, equalBtn;

    private TextView currentCalculation, previousCalculation;
    private ListView historyTextViewField;
    private LinearLayout historyTopBar, midleBar;

    private String container = "";
    private ImageView backBtn, historyBtn, goBackBtn;
    private long lastTouchTime = 0;
    private long currentTouchTime = 0;
    private boolean isHistoryVisible = false;
    private ConstraintLayout calculationButtons;

    private LinkedHashMap<String, String> historyMap = new LinkedHashMap<>();

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
            calculationButtons = findViewById(R.id.main_btn_container);
            historyTopBar = findViewById(R.id.hisory_bar);
            goBackBtn = findViewById(R.id.go_back_btn);
            midleBar = findViewById(R.id.middle_bar);

            currentCalculation = findViewById(R.id.equal_response);
            previousCalculation = findViewById(R.id.calculation_text_field);



            numberOneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "1";
                    previousCalculation.setText(container);
                }
            });


            numberTwoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "2";
                    previousCalculation.setText(container);
                }
            });

            numberThreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "3";
                    previousCalculation.setText(container);
                }
            });

            numberFourBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "4";
                    previousCalculation.setText(container);
                }
            });

            numberFiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "5";
                    previousCalculation.setText(container);
                }
            });

            numberSixBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "6";
                    previousCalculation.setText(container);
                }
            });

            numberSevenBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "7";
                    previousCalculation.setText(container);
                }
            });

            numberEightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "8";
                    previousCalculation.setText(container);
                }
            });

            numberNineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "9";
                    previousCalculation.setText(container);
                }
            });

            numberZeroBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!container.isEmpty()) {
                        container += "0";
                        previousCalculation.setText(container);
                    }
                }
            });

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container = "";
                    previousCalculation.setText(container);
                    currentCalculation.setText("");
                }
            });

            divisionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "÷";
                    previousCalculation.setText(container);
                }
            });

            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "+";
                    previousCalculation.setText(container);
                }
            });

            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "-";
                    previousCalculation.setText(container);
                }
            });

            precentsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += "%";
                    previousCalculation.setText(container);
                }
            });

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container = deleteLastCharacter(container);
                    previousCalculation.setText(container);
                }
            });

            dotBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container += ".";
                    previousCalculation.setText(container);
                }
            });

            historyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                            historyTextViewField.setVisibility(View.VISIBLE);
                            historyTopBar.setVisibility(View.VISIBLE);
                            previousCalculation.setVisibility(View.GONE);
                            calculationButtons.setVisibility(View.GONE);
                            midleBar.setVisibility(View.GONE);
                            currentCalculation.setVisibility(View.GONE);

                            List<String> historyList = new ArrayList<>();
                            for (Map.Entry<String, String> entry : historyMap.entrySet()) {
                                historyList.add(entry.getKey() + "\n" + " = " + entry.getValue());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, historyList);
                            historyTextViewField.setAdapter(adapter);




            };
            });

            goBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyTextViewField.setVisibility(View.GONE);
                    calculationButtons.setVisibility(View.VISIBLE);
                    historyTopBar.setVisibility(View.GONE);
                    midleBar.setVisibility(View.VISIBLE);
                    currentCalculation.setVisibility(View.VISIBLE);
                    previousCalculation.setVisibility(View.VISIBLE);
//                    goBackBtn.setVisibility(View.GONE);
                }
            });

            equalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastTouchTime = currentTouchTime;
                    currentTouchTime = System.currentTimeMillis();
                    String expressionStr = currentCalculation.getText().toString();


                    if (currentTouchTime - lastTouchTime < 2500) {

                        clearAllData();

                    } else if (expressionStr.isEmpty() && !checkLastChar()) {

                        try {

                            Expression expression = new ExpressionBuilder(container).build();

                            double result = expression.evaluate();

                            if (Math.abs(result - Math.round(result)) < 1e-9) {
                                int finalResult = (int) Math.round(result);
                                currentCalculation.setText(String.valueOf(finalResult));
                            } else {
                                currentCalculation.setText(String.format("%.2f", result));
                            }

                            if (!currentCalculation.getText().toString().isEmpty() && !previousCalculation.getText().toString().isEmpty()) {
                                historyMap.put(container, String.format(currentCalculation.getText().toString()));
                            }

                        } catch (Exception e) {
                            Log.e("CalcError", "Failed" + e);
                        }
                    }

                }
            });


            parenthesesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container = checkParentheses(container);
                    previousCalculation.setText(container);

                }
            });
        }


        public String deleteLastCharacter (String str){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) != 'x') {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }


        public String checkParentheses (String str){
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
                str += "(";
            }

            return str;
        }

        public void clearAllData () {
            if (currentCalculation.getText().toString().length() > 1) {
                previousCalculation.setText(currentCalculation.getText().toString());
                container = currentCalculation.getText().toString();
                currentCalculation.setText("");
            }
        }

        public boolean checkLastChar () {
            String str = currentCalculation.getText().toString();
            if (str.isEmpty()) return false;
            Set<Character> specialChars = new HashSet<>(Arrays.asList('(', '%', '÷', 'X', '-', '+'));

            return specialChars.contains(str.charAt(str.length() - 1));
        }


    }
