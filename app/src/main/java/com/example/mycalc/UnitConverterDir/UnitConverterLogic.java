package com.example.mycalc.UnitConverterDir;

import java.util.Map;

public class UnitConverterLogic {

    public double[] convert(double value1, double value2, String unit1, String unit2, String conversionType) {
        double[] result = new double[2];

        switch (conversionType.toLowerCase()) {
            case "area":
                result = convertArea(value1, value2, unit1, unit2);
                break;
            case "length":
                result = convertLength(value1, value2, unit1, unit2);
                break;
            case "temperature":
                result = convertTemperature(value1, value2, unit1, unit2);
                break;
            case "volume":
                result = convertVolume(value1, value2, unit1, unit2);
                break;
            case "mass":
                result = convertMass(value1, value2, unit1, unit2);
                break;
            case "data":
                result = convertData(value1, value2, unit1, unit2);
                break;
            case "speed":
                result = convertSpeed(value1, value2, unit1, unit2);
                break;
            case "time":
                result = convertTime(value1, value2, unit1, unit2);
                break;
            default:
                throw new IllegalArgumentException("Invalid conversion type.");
        }
        return result;
    }

    public double[] convertArea(double value1, double value2, String unit1, String unit2) {
        double value1InSquareMeters = convertToSquareMeters(value1, unit1);
        double convertedValue2 = convertFromSquareMeters(value1InSquareMeters, unit2);

        return new double[]{value1, Math.round(convertedValue2)};
    }

    private double[] convertLength(double value1, double value2, String unit1, String unit2) {
        // Implement length conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }

    private double[] convertTemperature(double value1, double value2, String unit1, String unit2) {
        // Implement temperature conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }

    private double[] convertVolume(double value1, double value2, String unit1, String unit2) {
        // Implement volume conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }

    private double[] convertMass(double value1, double value2, String unit1, String unit2) {
        // Implement mass conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }

    private double[] convertData(double value1, double value2, String unit1, String unit2) {
        // Implement data conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }

    private double[] convertSpeed(double value1, double value2, String unit1, String unit2) {
        // Implement speed conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }

    private double[] convertTime(double value1, double value2, String unit1, String unit2) {
        // Implement time conversion logic here
        double convertedValue = value1; // Placeholder for actual conversion logic
        return new double[]{value1, convertedValue};
    }



    private double convertToSquareMeters(double value, String unit) {
        switch (unit) {
            case "ac":
                return value * 4046.85642;
            case "a":
                return value * 100.0;
            case "ha":
                return value * 10000.0;
            case "cm²":
                return value * 0.0001;
            case "ft²":
                return value * 0.092903;
            case "in²":
                return value * 0.00064516;
            case "m²":
            default:
                return value;
        }
    }

    private double convertFromSquareMeters(double value, String unit) {
        switch (unit) {
            case "ac":
                return value / 4046.85642;
            case "a":
                return value / 100.0;
            case "ha":
                return value / 10000.0;
            case "cm²":
                return value / 0.0001;
            case "ft²":
                return value / 0.092903;
            case "in²":
                return value / 0.00064516;
            case "m²":
            default:
                return value;
        }
    }
}
