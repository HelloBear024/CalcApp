package com.example.mycalc.UnitConverter;

public class UnitConvertingLogic {

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

        return new double[]{value1, convertedValue2};
    }

    private double[] convertLength(double value1, double value2, String unit1, String unit2) {
        double valueInMeters = convertToMeters(value1, unit1);
        double convertedValue = convertFromMeters(valueInMeters, unit2);
        return new double[]{value1, convertedValue};
    }

    private double[] convertTemperature(double value1, double value2, String unit1, String unit2) {
        if (unit1.equals(unit2)) return new double[]{value1, value1};

        double valueInCelsius;
        switch (unit1) {
            case "C": valueInCelsius = value1; break;
            case "F": valueInCelsius = (value1 - 32) * 5.0 / 9.0; break;
            case "K": valueInCelsius = value1 - 273.15; break;
            default: throw new IllegalArgumentException("Invalid temperature unit: " + unit1);
        }

        double convertedValue;
        switch (unit2) {
            case "C": convertedValue = valueInCelsius; break;
            case "F": convertedValue = (valueInCelsius * 9.0 / 5.0) + 32; break;
            case "K": convertedValue = valueInCelsius + 273.15; break;
            default: throw new IllegalArgumentException("Invalid temperature unit: " + unit2);
        }

        return new double[]{value1, convertedValue};
    }

    private double[] convertVolume(double value1, double value2, String unit1, String unit2) {
        double valueInCubicMeters = convertToCubicMeters(value1, unit1);
        double convertedValue = convertFromCubicMeters(valueInCubicMeters, unit2);
        return new double[]{value1, convertedValue};
    }

    private double[] convertMass(double value1, double value2, String unit1, String unit2) {
        double valueInKilograms = convertToKilograms(value1, unit1);
        double convertedValue = convertFromKilograms(valueInKilograms, unit2);
        return new double[]{value1, convertedValue};
    }

    private double[] convertData(double value1, double value2, String unit1, String unit2) {
        double valueInBytes = convertToBytes(value1, unit1);
        double convertedValue = convertFromBytes(valueInBytes, unit2);
        return new double[]{value1, convertedValue};
    }

    private double[] convertSpeed(double value1, double value2, String unit1, String unit2) {
        double valueInMetersPerSecond = convertToMetersPerSecond(value1, unit1);
        double convertedValue = convertFromMetersPerSecond(valueInMetersPerSecond, unit2);
        return new double[]{value1, convertedValue};
    }

    private double[] convertTime(double value1, double value2, String unit1, String unit2) {
        double valueInSeconds = convertToSeconds(value1, unit1);
        double convertedValue = convertFromSeconds(valueInSeconds, unit2);
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


    private double convertToMeters(double value, String unit) {
        switch (unit) {
            case "mm": return value * 0.001;
            case "cm": return value * 0.01;
            case "m": return value;
            case "km": return value * 1000.0;
            case "in": return value * 0.0254;
            case "ft": return value * 0.3048;
            case "yd": return value * 0.9144;
            case "mi": return value * 1609.34;
            case "NM": return value * 1852.0;
            case "mil": return value * 0.0000254;
            default: throw new IllegalArgumentException("Invalid length unit: " + unit);
        }
    }

    private double convertFromMeters(double value, String unit) {
        switch (unit) {
            case "mm": return value / 0.001;
            case "cm": return value / 0.01;
            case "m": return value;
            case "km": return value / 1000.0;
            case "in": return value / 0.0254;
            case "ft": return value / 0.3048;
            case "yd": return value / 0.9144;
            case "mi": return value / 1609.34;
            case "NM": return value / 1852.0;
            case "mil": return value / 0.0000254;
            default: throw new IllegalArgumentException("Invalid length unit: " + unit);
        }
    }




    private double convertToCubicMeters(double value, String unit) {
        switch (unit) {
            case "uk gal": return value * 0.00454609;
            case "us gal": return value * 0.00378541;
            case "l": return value * 0.001;
            case "ml": return value * 1e-6;
            case "cc cm²": return value * 1e-6;
            case "m³": return value;
            case "in³": return value * 1.63871e-5;
            case "ft³": return value * 0.0283168;
            default: throw new IllegalArgumentException("Invalid volume unit: " + unit);
        }
    }

    private double convertFromCubicMeters(double value, String unit) {
        switch (unit) {
            case "uk gal": return value / 0.00454609;
            case "us gal": return value / 0.00378541;
            case "l": return value / 0.001;
            case "ml": return value / 1e-6;
            case "cc cm²": return value / 1e-6;
            case "m³": return value;
            case "in³": return value / 1.63871e-5;
            case "ft³": return value / 0.0283168;
            default: throw new IllegalArgumentException("Invalid volume unit: " + unit);
        }
    }



    private double convertToKilograms(double value, String unit) {
        switch (unit) {
            case "t": return value * 1000.0;
            case "uk t": return value * 1016.05;
            case "us t" : return value * 907.185;
            case "lb": return value * 0.453592;
            case "oz": return value * 0.0283495;
            case "kg": return value;
            case "g": return value * 0.001;
            default: throw new IllegalArgumentException("Invalid mass unit: " + unit);
        }
    }

    private double convertFromKilograms(double value, String unit) {
        switch (unit) {
            case "t": return value / 1000.0;
            case "uk t": return value / 1016.05;
            case "us t": return value / 907.185;
            case "lb": return value / 0.453592;
            case "oz": return value / 0.0283495;
            case "kg": return value;
            case "g": return value / 0.001;
            default: throw new IllegalArgumentException("Invalid mass unit: " + unit);
        }
    }



    private double convertToBytes(double value, String unit) {
        switch (unit) {
            case "bit": return value / 8.0;
            case "B": return value;
            case "KB": return value * 1000.0;
            case "KiB": return value * 1024.0;
            case "MB": return value * 1e6;
            case "MiB": return value * 1048576.0;
            case "GB": return value * 1e9;
            case "GiB": return value * 1.073741824e9;
            case "TB": return value * 1e12;
            case "TiB": return value * 1.099511627776e12;
            default: throw new IllegalArgumentException("Invalid data unit: " + unit);
        }
    }

    private double convertFromBytes(double value, String unit) {
        switch (unit) {
            case "bit": return value * 8.0;
            case "B": return value;
            case "KB": return value / 1000.0;
            case "KiB": return value / 1024.0;
            case "MB": return value / 1e6;
            case "MiB": return value / 1048576.0;
            case "GB": return value / 1e9;
            case "GiB": return value / 1.073741824e9;
            case "TB": return value / 1e12;
            case "TiB": return value / 1.099511627776e12;
            default: throw new IllegalArgumentException("Invalid data unit: " + unit);
        }
    }

    private double convertToMetersPerSecond(double value, String unit) {
        switch (unit) {
            case "m/s": return value;
            case "m/h": return value / 3600.0;
            case "km/s": return value * 1000.0;
            case "km/h": return value / 3.6;
            case "in/s": return value * 0.0254;
            case "in/h": return value * 0.0254 / 3600.0;
            case "ft/s": return value * 0.3048;
            case "ft/h": return value * 0.3048 / 3600.0;
            case "mi/s": return value * 1609.34;
            case "mi/h": return value * 0.44704;
            case "kn": return value * 0.514444;
            default: throw new IllegalArgumentException("Invalid speed unit: " + unit);
        }
    }

    private double convertFromMetersPerSecond(double value, String unit) {
        switch (unit) {
            case "m/s":
                return value;
            case "m/h":
                return value * 3600.0;
            case "km/s":
                return value / 1000.0;
            case "km/h":
                return value * 3.6;
            case "in/s":
                return value / 0.0254;
            case "in/h":
                return value / 0.0254 * 3600.0;
            case "ft/s":
                return value / 0.3048;
            case "ft/h":
                return value / 0.3048 * 3600.0;
            case "mi/s":
                return value / 1609.34;
            case "mi/h":
                return value / 0.44704;
            case "kn":
                return value / 0.514444;
            default:
                throw new IllegalArgumentException("Invalid speed unit: " + unit);
        }
    }
    private double convertToSeconds(double value, String unit) {
        switch (unit) {
            case "ms": return value / 1000.0;
            case "s": return value;
            case "min": return value * 60.0;
            case "h": return value * 3600.0;
            case "d": return value * 86400.0;
            case "wk": return value * 604800.0;
            default: throw new IllegalArgumentException("Invalid time unit: " + unit);
        }
    }

    private double convertFromSeconds(double value, String unit) {
        switch (unit) {
            case "ms": return value * 1000.0;
            case "s": return value;
            case "min": return value / 60.0;
            case "h": return value / 3600.0;
            case "d": return value / 86400.0;
            case "wk": return value / 604800.0;
            default: throw new IllegalArgumentException("Invalid time unit: " + unit);
        }
    }
}
