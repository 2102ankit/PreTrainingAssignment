package basics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataTypes {
    static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        //value from -128 to 127
        byte oldExpenses = 100;
        byte newExpenses = 20;
        byte netExpenses = (byte)(oldExpenses + newExpenses);
        logger.debug("net expenses : {}", netExpenses);

        //value from -32768 to 32768
        short salaryJan = 15000;
        short salaryFeb = 15000;
        short totalSalary = (short) (salaryJan + salaryFeb);
        logger.debug("total salary : {}", totalSalary);

        //type casting order byte > short > char > int > long > float > double
        byte byteValue = 9;
        short shortValue = 2000;
        int intValue = 30000000;
        long longValue = 1000000000;
        float floatValue = 123456789.987654321f;
        double doubleValue = 987654321.123456789d;

        double ans = doubleValue + floatValue + longValue + intValue + shortValue + byteValue;
        logger.debug(ans);
    }
}
