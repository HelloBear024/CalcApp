package com.example.mycalc.Calculator.CalculatorHelpers;

import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CalculationHelper {

    private String container = "";

    public void appendToContainerOperation(String value) {
        if (container.isEmpty() && isOperator(value.charAt(0))) {
            return;
        }

        if (!container.isEmpty()) {
            char lastChar = container.charAt(container.length() - 1);
            if (isOperator(lastChar)) {
                container = container.substring(0, container.length() - 1);
            }
        }

        container += value;
    }


    public void appendToContainerNumber(String value) {
        container += value;
    }


    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '.';
    }

    public boolean checkLastChar() {
        if (container.isEmpty()) {
            return false;
        }
        char lastChar = container.charAt(container.length() - 1);
        return isOperator(lastChar);
    }

    public String getContainer() {
        return container;
    }


    public void clearContainer() {
        container = "";
    }

    public void setContainer(String value) {
        container = value;
    }

    public String checkParentheses(String container) {
        if (container == null) return null;
        int openCount = 0;
        int closeCount = 0;
        for (int i = 0; i < container.length(); i++) {
            if (container.charAt(i) == '(') openCount++;
            if (container.charAt(i) == ')') closeCount++;
        }
        if (openCount > closeCount) {
            if (container.length() > 0 && (Character.isDigit(container.charAt(container.length() - 1)) || container.charAt(container.length() - 1) == ')')) {
                container += ")";
            } else {
                container += "(";
            }
        } else {
            if (container.length() > 0 && (Character.isDigit(container.charAt(container.length() - 1)) || container.charAt(container.length() - 1) == ')')) {
                container += "*(";
            } else {
                container += "(";
            }
        }
        return container;
    }

    public String deleteLastCharacter(String container) {
        if (container != null && container.length() > 0 && container.charAt(container.length() - 1) != 'x') {
            container = container.substring(0, container.length() - 1);
        }
        return container;
    }

    public String togglePlusMinus(String container) {
        if (!container.isEmpty()) {
            int lastOperatorIndex = -1;
            for (int i = container.length() - 1; i >= 0; i--) {
                char c = container.charAt(i);
                if (c == '+' || c == '*' || c == '/' || c == '%') {
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
            }
        }
        return container;
    }

    public void clearAllData(TextView previousCalculation, TextView currentCalculation) {
        String result = currentCalculation.getText().toString();
        if (!result.isEmpty()) {
            previousCalculation.setText(result);
            container = result;
        }
        // Clear the container for new calculation
        currentCalculation.setText("");

    }

    public void appendFunction(String function) {
        if (!container.isEmpty()) {
            char lastChar = container.charAt(container.length() - 1);
            if (function.equals("^(3)")) {
                if (Character.isDigit(lastChar)) {
                    container += function;
                    return;
                } else if (isOperator(lastChar)) {
                    container = container.substring(0, container.length() - 1) + function;
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

        container += function;
    }
}
