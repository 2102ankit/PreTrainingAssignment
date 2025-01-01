package oopswithexceptions.inheritance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Calculation {
    double ans;

    public double getCalculationResult(double num1, double num2, String operation) {
        switch (operation) {
            case "add":
                ans = num1 + num2;
                break;
            case "subtract":
                ans = num1 - num2;
                break;
            case "multiply":
                ans = num1 * num2;
                break;
            case "divide":
                if (num2 != 0) {
                    ans = num1 / num2;
                } else {
                    throw new ArithmeticException("Can't Divide by zero");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
        return ans;
    }
}

class ExtendedCalculation extends Calculation {
    double ans;

    @Override
    public double getCalculationResult(double num1, double num2, String operation) {
        switch (operation) {
            case "add":
            case "subtract":
            case "multiply":
            case "divide":
                return super.getCalculationResult(num1, num2, operation);
            case "exponentiation":
                ans = Math.pow(num1, num2);
            case "logarithm":
                if (num1 <= 0) {
                    throw new IllegalArgumentException("Log base cannot be negative");
                }
                return Math.log(num1);
            case "logBase":
//              if num1 is 1 the divisor becomes zero, and we cannot divide by zero
                if (num1 <= 0 || num2 <= 0 || num1 == 1) {
                    throw new IllegalArgumentException("Invalid Values for log base");
                }
                return Math.log(num2) / Math.log(num1);
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
}

public class Main {
    static final Logger logger = LogManager.getLogger();

    public static void performCalculation(Calculation calculator, double num1, double num2, String operation){
        double operationResult;

        try{
            operationResult = calculator.getCalculationResult(num1,num2, operation);
            logger.debug("{} : {}", operation.substring(0,1).toUpperCase() + operation.substring(1), operationResult);
        }catch (ArithmeticException e){
            logger.error("Error in calculation : {}", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Invalid Operation : {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        Calculation calculator = new Calculation();
        double operationResult;

        // Perform standard calculations
        performCalculation(calculator, 10, 20, "add");
        performCalculation(calculator, 30, 15, "subtract");
        performCalculation(calculator, 4, 7, "multiply");
        performCalculation(calculator, 100, 20, "divide");
        performCalculation(calculator, 1, 0, "divide"); // Faulty division

        // Perform additional calculations
        performCalculation(calculator, 23, 33, "exponentiation");

        // Perform calculations with extended functionality
        ExtendedCalculation extendedCalculator = new ExtendedCalculation();
        performCalculation(extendedCalculator, 23, 33, "exponentiation");
        performCalculation(extendedCalculator, 100, 0, "logarithm");
        performCalculation(extendedCalculator, 0, 10, "logarithm");
        performCalculation(extendedCalculator, 1024, 512, "logBase");
        performCalculation(extendedCalculator, 1024, -512, "logBase");
        performCalculation(extendedCalculator, 1024, -512, "sine");

    }
}
