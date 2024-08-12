package com.example.mycalc.Calculator.CalculatorHelpers;

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

    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '.';
    }




    public String getContainer() {
        return container;
    }

}
