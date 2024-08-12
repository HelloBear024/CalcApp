package com.example.mycalc.Calculator.CalculatorHelpers;
import net.objecthunter.exp4j.function.Function;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

public class CustomFunctionsForArithmetic {

    public static List<Function> getFunctions() {
        List<Function> functions = new ArrayList<>();

        functions.add(new Function("sin", 1) {
            @Override
            public double apply(double... args) {
                return Math.sin(Math.toRadians(args[0]));
            }
        });

        functions.add(new Function("cos", 1) {
            @Override
            public double apply(double... args) {
                return Math.cos(Math.toRadians(args[0]));
            }
        });

        functions.add(new Function("tan", 1) {
            @Override
            public double apply(double... args) {
                return Math.tan(Math.toRadians(args[0]));
            }
        });

        functions.add(new Function("asin", 1) {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(Math.asin(args[0]));
            }
        });

        functions.add(new Function("asinh", 1) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0] + Math.sqrt(args[0] * args[0] + 1));
            }
        });

        functions.add(new Function("acos", 1) {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(Math.acos(args[0]));
            }
        });

        functions.add(new Function("acosh", 1) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0] + Math.sqrt(args[0] * args[0] - 1));
            }
        });


        functions.add(new Function("atan", 1) {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(Math.atan(args[0]));
            }
        });

        functions.add(new Function("atanh", 1) {
            @Override
            public double apply(double... args) {
                return 0.5 * Math.log((1 + args[0]) / (1 - args[0]));
            }
        });

        functions.add(new Function("sqrt", 1) {
            @Override
            public double apply(double... args) {
                return Math.sqrt(args[0]);
            }
        });

        functions.add(new Function("cbrt", 1) {
            @Override
            public double apply(double... args) {
                return Math.cbrt(args[0]);
            }
        });

        functions.add(new Function("log", 1) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]);
            }
        });

        functions.add(new Function("log10", 1) {
            @Override
            public double apply(double... args) {
                return Math.log10(args[0]);
            }
        });

        functions.add(new Function("sinh", 1) {
            @Override
            public double apply(double... args) {
                return Math.sinh(args[0]);
            }
        });

        functions.add(new Function("cosh", 1) {
            @Override
            public double apply(double... args) {
                return Math.cosh(args[0]);
            }
        });

        functions.add(new Function("tanh", 1) {
            @Override
            public double apply(double... args) {
                return Math.tanh(args[0]);
            }
        });

        functions.add(new Function("exp", 1) {
            @Override
            public double apply(double... args) {
                return Math.exp(args[0]);
            }
        });
        functions.add(new Function("fact", 1) {
            @Override
            public double apply(double... args) {
                return factorial((int) args[0]);
            }

            private double factorial(int n) {
                if (n < 0) throw new IllegalArgumentException("Negative number!");
                if (n == 0) return 1;
                double result = 1;
                for (int i = 1; i <= n; i++) {
                    result *= i;
                }
                return result;
            }
        });

        functions.add(new Function("abs", 1) {
            @Override
            public double apply(double... args) {
                return Math.abs(args[0]);
            }
        });
        return functions;
    }


}
