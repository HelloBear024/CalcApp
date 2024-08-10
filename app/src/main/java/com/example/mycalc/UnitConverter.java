package com.example.mycalc;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mycalc.UnitConverterDir.UnitConverterLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitConverter extends AppCompatActivity {

    Button  button_one, button_two, button_three,
            button_four, button_five, button_six, button_seven, button_eight,
            button_nine, button_zero, button_dot, delete_previous_character,
            delete_all_characters;

    ImageButton goToMainCalculator,  go_down, go_up;
    RadioGroup radioGroup;
    Spinner spinner_top, spinner_bottom;
    EditText editText_top, editText_bottom;
    TextView topTextView, bottomTextView;
    Vibrator vibe;
    String container_top = "", container_bottom = "";
    String conversionType = "area";

    private List<String> area_data = Arrays.asList("Acres (ac)", "Ares (a)", "Hectares (ha)", "Square centimeters (cm²)", "Square feet (ft²)", "Square inches (in²)", "Square meters (m²)");
    private List<String> length_data = Arrays.asList("Millimetres (mm)", "Centimetres (cm)", "Metres (m)", "Kilometres (km)", "Inches (in)", "Feet (ft)", "Yards (yd)", "Miles (mi)", "Nautical miles (NM)", "Mils (mil)");
    private List<String> temperature_data = Arrays.asList("Celsius (C)", "Fahrenheit (F)", "Kelvin (K)");
    private List<String> volume_data = Arrays.asList("UK gallons (gal)", " US gallons (gal)", "Litres (l)", "Millilitres (ml)", "Cubic centimetres (cc) (cm²)", "Cubic metres (m³)", "Cubic inches (in³)", "Cubic feet (ft³)");
    private List<String> mass_data = Arrays.asList("Tons (t)", "UK tons (t)", "US tons (t)", "Pounds (lb)", "Ounces (oz)", "Kilogrammes (kg)", "Grams (g)");
    private List<String> data_data = Arrays.asList("Bits (bit)", "Bytes (B)", "Kilobytes (KB)", "Kibibytes (KiB)", "Megabytes (MB)", "Mebibytes (MiB)", "Gigabytes (GB)", "Gibibyes (GiB)", "Terabytes (TB)", "Tebibytes (TiB)");
    private List<String> speed_data = Arrays.asList("Metres per second (m/s)", "Metres per hour (m/h)", "Kilometres per second (km/s)", "Kilometres per hour (km/h)", "Inches per second (in/s)", "Inches per hour (in/s)",
                                                    "Feet per second (ft/s)", "Feet per hour (ft/h)", "Miles per second (mi/s)", "Miles per hour (mi/h)", "Knots (kn)");
    private List<String> time_data = Arrays.asList("Milliseconds (ms)", "Seconds (s)", "Minutes (min)", "Hours (h)", "Days (d)", "Weeks (wk)");
    private List<String> tip_data = Arrays.asList("", "");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unit_converter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initViews();
        setNumberButtonListeners();

        editText_top.requestFocus();
        editText_top.setCursorVisible(true);

        go_up.setEnabled(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, area_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_top.setAdapter(adapter);
        spinner_bottom.setAdapter(adapter);

        spinner_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String extractedText = extractTextFromParentheses(selectedItem);
                topTextView.setText(extractedText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_bottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String extractedText = extractTextFromParentheses(selectedItem);
                bottomTextView.setText(extractedText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_area) {
                    updateSpinnerData(area_data);
                    conversionType = "area";

                } else if (checkedId == R.id.radio_button_length) {
                    updateSpinnerData(length_data);
                    conversionType = "length";

                } else if (checkedId == R.id.radio_button_temperature){
                    updateSpinnerData(temperature_data);
                    conversionType = "temperature";

                } else if(checkedId == R.id.radio_button_volume){
                    updateSpinnerData(volume_data);
                    conversionType = "volume";

                } else if(checkedId == R.id.radio_button_mass){
                    updateSpinnerData(mass_data);
                    conversionType = "mass";

                } else if(checkedId == R.id.radio_button_data){
                    updateSpinnerData(data_data);
                    conversionType = "data";
                } else if(checkedId == R.id.radio_button_speed){
                    updateSpinnerData(speed_data);
                    conversionType = "speed";
                } else if(checkedId == R.id.radio_button_time){
                    updateSpinnerData(time_data);
                    conversionType = "time";
                } else if(checkedId == R.id.radio_button_tip){
                    updateSpinnerData(tip_data);
                    conversionType = "tip";
                }
            }
        });

        goToMainCalculator = findViewById(R.id.go_to_main_calendar);

        goToMainCalculator.setOnClickListener(v -> {
            Intent arithmeticCalculator = new Intent(UnitConverter.this, MainActivity.class);
            startActivity(arithmeticCalculator);
        });



        editText_top.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText_top.setCursorVisible(true);
//                    editText_top.setSelection(editText_top.getText().toString().length());
                    go_down.setEnabled(true);
                    go_up.setEnabled(false);
                }
            }
        });

        go_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_bottom.clearFocus();
                editText_top.requestFocus();
                editText_top.setCursorVisible(true);
//                editText_top.setSelection(editText_top.getText().length());
                go_up.setEnabled(false);
                go_down.setEnabled(true);
            }
        });

        go_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_top.clearFocus();
                editText_bottom.requestFocus();
                editText_bottom.setCursorVisible(true);
                go_down.setEnabled(false);
                go_up.setEnabled(true);
            }
        });

        suppressKeyboard(editText_top);
        suppressKeyboard(editText_bottom);


        editText_bottom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText_bottom.setCursorVisible(true);
                    editText_bottom.setSelection(editText_bottom.getText().length());
                    go_down.setEnabled(false);
                    go_up.setEnabled(true);
                }
            }
        });
    }

    private void updateSpinnerData(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_top.setAdapter(adapter);
        spinner_bottom.setAdapter(adapter);
    }

    private void initViews() {
        go_down = findViewById(R.id.arrow_down);
        go_up = findViewById(R.id.arrow_up);
        editText_top = findViewById(R.id.unit_converter_first_input);
        editText_bottom = findViewById(R.id.unit_converter_second_input);
        topTextView = findViewById(R.id.unit_converter_text_view_first);
        bottomTextView = findViewById(R.id.unit_converter_text_view_second);
        goToMainCalculator = findViewById(R.id.go_to_main_calendar);
        spinner_top = findViewById(R.id.unit_converter_first_spinner);
        spinner_bottom = findViewById(R.id.unit_converter_second_spinner);
        button_one = findViewById(R.id.one_btn);
        button_two = findViewById(R.id.two_btn);
        button_three = findViewById(R.id.three_btn);
        button_four = findViewById(R.id.four_btn);
        button_five = findViewById(R.id.five_btn);
        button_six = findViewById(R.id.six_btn);
        button_seven = findViewById(R.id.seven_btn);
        button_eight = findViewById(R.id.eight_btn);
        button_nine = findViewById(R.id.nine_btn);
        button_zero = findViewById(R.id.zero_btn);
    }

    private void setNumberButtonListeners() {
        setButtonListenerWithResizeNumber(button_one, "1");
        setButtonListenerWithResizeNumber(button_two, "2");
        setButtonListenerWithResizeNumber(button_three, "3");
        setButtonListenerWithResizeNumber(button_four, "4");
        setButtonListenerWithResizeNumber(button_five, "5");
        setButtonListenerWithResizeNumber(button_six, "6");
        setButtonListenerWithResizeNumber(button_seven, "7");
        setButtonListenerWithResizeNumber(button_eight, "8");
        setButtonListenerWithResizeNumber(button_nine, "9");
        setButtonListenerWithResizeNumber(button_zero, "0");
    }

    private String extractTextFromParentheses(String text) {
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }




    private void suppressKeyboard(EditText editText) {
        editText.setShowSoftInputOnFocus(false);  // Disable the soft keyboard
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    editText.requestFocus();
                    editText.setCursorVisible(true);
                    // Ensure the cursor position can be adjusted by the user
                    return false; // Returning false allows the touch event to be processed normally
                }
                return false; // Returning false allows the touch event to be processed normally
            }
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


    // number is appended to the back but cursor doesn't move
//    private void appendToContainerNumber(String value) {
//        if (editText_top.isFocused()) {
//            // Append the new character at the end of the text
//            container_top = editText_top.getText().toString() + value;
//            editText_top.setText(container_top);
//            // Set the cursor at the end of the text
//            editText_top.setSelection(container_top.length());
//        } else if (editText_bottom.isFocused()) {
//            // Append the new character at the end of the text
//            container_bottom = editText_bottom.getText().toString() + value;
//            editText_bottom.setText(container_bottom);
//            // Set the cursor at the end of the text
//            editText_bottom.setSelection(container_bottom.length());
//        }
//        performConversion();
//    }


    private void appendToContainerNumber(String value) {
        if (editText_top.isFocused()) {
            // Get the current text from the EditText
            Editable currentText = editText_top.getText();
            // Get the current cursor position
            int start = editText_top.getSelectionStart();
            // Insert the new character at the cursor position
            currentText.insert(start, value);
            // Set the text back to the EditText (not strictly necessary since we're modifying the Editable directly)
            editText_top.setText(currentText);
            // Move the cursor to the right after insertion
            editText_top.setSelection(start + 1);
        } else if (editText_bottom.isFocused()) {
            // Get the current text from the EditText
            Editable currentText = editText_bottom.getText();
            // Get the current cursor position
            int start = editText_bottom.getSelectionStart();
            // Insert the new character at the cursor position
            currentText.insert(start, value);
            // Set the text back to the EditText (not strictly necessary since we're modifying the Editable directly)
            editText_bottom.setText(currentText);
            // Move the cursor to the right after insertion
            editText_bottom.setSelection(start + 1);
        }
        // Perform the conversion after updating the text
        performConversion();
    }



//    private void appendToContainerNumber(String value) {
//        if (editText_top.isFocused()) {
//            int start = editText_top.getSelectionStart(); // Get current cursor position
//            container_top = new StringBuilder(container_top).insert(start, value).toString();
//            editText_top.setText(container_top);
//            editText_top.setSelection(start + value.length()); // Move cursor to the right after insertion
//        } else if (editText_bottom.isFocused()) {
//            int start = editText_bottom.getSelectionStart(); // Get current cursor position
//            container_bottom = new StringBuilder(container_bottom).insert(start, value).toString();
//            editText_bottom.setText(container_bottom);
//            editText_bottom.setSelection(start + value.length()); // Move cursor to the right after insertion
//        }
//        performConversion();
//    }







    public void clearAllData() {
        container_top = "";
        container_bottom = "";
        editText_top.setText(container_top);
        editText_bottom.setText(container_bottom);
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

    private void performConversion() {
        String topUnit = topTextView.getText().toString();
        String bottomUnit = bottomTextView.getText().toString();

        String inputTextTop = editText_top.getText().toString();
        String inputTextBottom = editText_bottom.getText().toString();
        long edit_view_container = 0;
        long value2 = 0;

        if (!inputTextTop.isEmpty() && editText_top.isFocused()) {
            try {
                edit_view_container = Long.parseLong(inputTextTop);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number in the top field", Toast.LENGTH_SHORT).show();
                return; // Exit the method if the input is invalid
            }
        }

        if (!inputTextBottom.isEmpty() && editText_bottom.isFocused()) {
            try {
                value2 = Long.parseLong(inputTextBottom);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number in the bottom field", Toast.LENGTH_SHORT).show();
                return; // Exit the method if the input is invalid
            }
        }

        UnitConverterLogic unitConverter = new UnitConverterLogic();
        double[] result = unitConverter.convert(edit_view_container, value2, topUnit, bottomUnit, conversionType);

        // Round the double result to long (integer) before displaying
        long displayValue1 = Math.round(result[0]);
        long displayValue2 = Math.round(result[1]);

        // Only update the non-focused EditText to avoid disrupting the cursor
        if (editText_top.isFocused()) {
            editText_bottom.setText(String.valueOf(displayValue2));
        } else {
            editText_top.setText(String.valueOf(displayValue2));
        }
    }


}