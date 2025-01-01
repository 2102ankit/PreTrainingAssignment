package oopswithexceptions.encapsulation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

enum AccountType {
    SAVINGS, CURRENT, BUSINESS, SALARY
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

abstract class BankAccount {
    private double balance;
    private AccountType accountType;


    public BankAccount(AccountType accountType, double balance) {
        if (balance < 0) throw new IllegalArgumentException("Cannot initialize balance to zero");
        this.accountType = accountType;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void depositMoney(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit Amount cannot be Negative");
        }
        balance += amount;
    }

    public void withdrawMoney(double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal Amount cannot be Negative");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Withdrawal Amount cannot be greater than Balance");
        }
        balance -= amount;
    }

    // Abstract method to get Interest frequency based on type of account
    abstract public int getInterestFrequency();

    // Abstract method to get the interest rate (to be implemented by subclasses)
    public abstract double getInterestRate();

    // Abstract method to calculate interest (also to be implemented by subclasses)
    public abstract double calculateInterest();

}

class SavingsAccount extends BankAccount {
    private static final double INTEREST_RATE = 0.03;
    private static final int INTEREST_FREQUENCY = 12; // Monthly compounding


    public SavingsAccount(double balance) {
        super(AccountType.SAVINGS, balance);
    }

    @Override
    public int getInterestFrequency() {
        return INTEREST_FREQUENCY;
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public double calculateInterest() {
        // Formula for compound interest: A = P*(1 + r/n)^(nt)
        // so I = A - P = P * (1+ r/n)^(nt) - P
        double principal = getBalance();
        double rateOfInterest = getInterestRate();
        double frequencyOfCompounding = getInterestFrequency();
        int numberOfYears = 1; //assumed

        double compoundInterest = principal * Math.pow(1 + rateOfInterest / frequencyOfCompounding, frequencyOfCompounding * numberOfYears) - principal;

        return compoundInterest;
    }
}

class CurrentAccount extends BankAccount {
    private static final double INTEREST_RATE = 0.001;
    private static final int INTEREST_FREQUENCY = 12;

    public CurrentAccount(double balance) {
        super(AccountType.CURRENT, balance);
    }

    @Override
    public int getInterestFrequency() {
        return 0;
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public double calculateInterest() {
        return getBalance() * getInterestRate();
    }
}

class BusinessAccount extends BankAccount {
    //    private static final double INTEREST_RATE = 0;
    private static final int INTEREST_FREQUENCY = 12;

    public BusinessAccount(double balance) {
        super(AccountType.BUSINESS, balance);
    }


    @Override
    public int getInterestFrequency() {
        return INTEREST_FREQUENCY;
    }

    @Override
    public double getInterestRate() {
        double balance = getBalance();
        if (balance <= 100000) {
            return 0.01; //say 1%
        } else return 0.015; //1.5%
    }

    @Override
    public double calculateInterest() {
        double balance = getBalance();
        double interest;
        if (balance < 100000) {
            interest = balance * 0.01;
        } else {
            interest = 100000 * 0.01 + (balance - 100000) * 0.015;
        }
        return interest;
    }
}

class SalaryAccount extends BankAccount {
    private static final double INTEREST_RATE = 0.005;
    private static final int INTEREST_FREQUENCY = 4; //quarterly

    public SalaryAccount(double balance) {
        super(AccountType.SALARY, balance);
    }


    @Override
    public int getInterestFrequency() {
        return INTEREST_FREQUENCY;
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public double calculateInterest() {
        // Formula for compound interest: A = P*(1 + r/n)^(nt)
        // so I = A - P = P * (1+ r/n)^(nt) - P
        double principal = getBalance();
        double rateOfInterest = getInterestRate();
        double frequencyOfCompounding = getInterestFrequency();
        int numberOfYears = 1; //assumed

        double compoundInterest = principal * Math.pow(1 + rateOfInterest / frequencyOfCompounding, frequencyOfCompounding * numberOfYears) - principal;

        return compoundInterest;
    }
}

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            // Instantiate different accounts
            SavingsAccount savingsAccount = new SavingsAccount(10000);
            CurrentAccount currentAccount = new CurrentAccount(5000);
            BusinessAccount businessAccount = new BusinessAccount(150000);
            SalaryAccount salaryAccount = new SalaryAccount(20000);

            // Perform operations on Savings Account
            logger.debug("Savings Account Initial Balance: " + savingsAccount.getBalance());
            savingsAccount.depositMoney(2000);
            logger.debug("Savings Account Balance after Deposit: " + savingsAccount.getBalance());
            savingsAccount.withdrawMoney(500);
            logger.debug("Savings Account Balance after Withdrawal: " + savingsAccount.getBalance());
            double savingsInterest = savingsAccount.calculateInterest();
            logger.debug("Savings Account Balance after Interest Calculation: " + (savingsAccount.getBalance() + savingsInterest));

            // Perform operations on Current Account
            logger.debug("Current Account Initial Balance: " + currentAccount.getBalance());
            currentAccount.depositMoney(1000);
            logger.debug("Current Account Balance after Deposit: " + currentAccount.getBalance());
            currentAccount.withdrawMoney(200);
            logger.debug("Current Account Balance after Withdrawal: " + currentAccount.getBalance());
            double currentInterest = currentAccount.calculateInterest();
            logger.debug("Current Account Balance after Interest Calculation: " + (currentAccount.getBalance() + currentInterest));

            // Perform operations on Business Account
            logger.debug("Business Account Initial Balance: " + businessAccount.getBalance());
            businessAccount.depositMoney(30000);
            logger.debug("Business Account Balance after Deposit: " + businessAccount.getBalance());
            businessAccount.withdrawMoney(10000);
            logger.debug("Business Account Balance after Withdrawal: " + businessAccount.getBalance());
            double businessInterest = businessAccount.calculateInterest();
            logger.debug("Business Account Balance after Interest Calculation: " + (businessAccount.getBalance() + businessInterest));

            // Perform operations on Salary Account
            logger.debug("Salary Account Initial Balance: " + salaryAccount.getBalance());
            salaryAccount.depositMoney(5000);
            logger.debug("Salary Account Balance after Deposit: " + salaryAccount.getBalance());
            salaryAccount.withdrawMoney(1000);
            logger.debug("Salary Account Balance after Withdrawal: " + salaryAccount.getBalance());
            double salaryInterest = salaryAccount.calculateInterest();
            logger.debug("Salary Account Balance after Interest Calculation: " + (salaryAccount.getBalance() + salaryInterest));

        } catch (InsufficientFundsException | IllegalArgumentException e) {
            logger.error("An error occurred: " + e.getMessage());
        }

        try {
            // Attempt to initialize an account with a negative balance
            logger.debug("Trying to create a Savings Account with a negative balance...");
            SavingsAccount invalidAccount = new SavingsAccount(-1000); // This will throw an error
        } catch (IllegalArgumentException e) {
            logger.error("Error: " + e.getMessage());
        }

        try {
            // Create a valid Savings Account
            SavingsAccount savingsAccount = new SavingsAccount(5000);

            // Attempt to withdraw more than the balance
            logger.debug("Trying to withdraw more than the available balance...");
            savingsAccount.withdrawMoney(6000); // This will throw InsufficientFundsException
        } catch (InsufficientFundsException e) {
            logger.error("Error: " + e.getMessage());
        }

        try {
            // Create a valid Business Account
            BusinessAccount businessAccount = new BusinessAccount(150000);

            // Attempt to deposit a negative amount
            logger.debug("Trying to deposit a negative amount...");
            businessAccount.depositMoney(-500); // This will throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            logger.error("Error: " + e.getMessage());
        }

        try {
            // Create a valid Current Account
            CurrentAccount currentAccount = new CurrentAccount(5000);

            // Attempt to withdraw a negative amount
            logger.debug("Trying to withdraw a negative amount...");
            currentAccount.withdrawMoney(-100); // This will throw IllegalArgumentException
        } catch (IllegalArgumentException | InsufficientFundsException e) {
            logger.error("Error: " + e.getMessage());
        }

        try {
            // Create a valid Salary Account
            SalaryAccount salaryAccount = new SalaryAccount(20000);

            // Attempt to set a negative balance
            logger.debug("Trying to set a negative balance directly...");
            salaryAccount.setBalance(-1000); // This will throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            logger.error("Error: " + e.getMessage());
        }

    }
}

