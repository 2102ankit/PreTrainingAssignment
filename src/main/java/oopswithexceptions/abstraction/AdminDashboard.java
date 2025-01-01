package oopswithexceptions.abstraction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

abstract class Employee {
    protected String name;
    protected int id;
    protected double baseSalary;

    public Employee(String name, int id, double baseSalary) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0.");
        }
        if (baseSalary < 0) {
            throw new IllegalArgumentException("Base salary cannot be negative.");
        }
        this.name = name;
        this.id = id;
        this.baseSalary = baseSalary;
    }

    public abstract double calculateSalary();

    public abstract void trackWorkHours();

    public String getEmployeeDetails() {
        return "Employee ID : " + this.id + ", Name : " + this.name + ", Base salary : " + this.baseSalary;
    }
}

interface Benefits {
    double calculateBonus();

    String getInsuranceDetails();
}

interface PerformanceEvaluation {
    void addPerformanceRating(int rating);

    String getPerformanceSummary();
}

class FullTimeEmployee extends Employee implements Benefits, PerformanceEvaluation {

    private double yearlyBonus;
    private String insuranceDetails;
    private int performanceRating;

    public FullTimeEmployee(String name, int id, double baseSalary, double yearlyBonus, String insuranceDetails) {
        super(name, id, baseSalary);
        if (yearlyBonus < 0) {
            throw new IllegalArgumentException("Yearly bonus cannot be negative.");
        }
        if (insuranceDetails == null || insuranceDetails.isEmpty()) {
            throw new IllegalArgumentException("Insurance details cannot be null or empty.");
        }
        this.yearlyBonus = yearlyBonus;
        this.insuranceDetails = insuranceDetails;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + yearlyBonus;
    }

    @Override
    public void trackWorkHours() {
        Logger logger = LogManager.getLogger();
        logger.debug("Full-time employee works 40 hours per week.");
    }

    @Override
    public double calculateBonus() {
        return yearlyBonus;
    }

    @Override
    public String getInsuranceDetails() {
        return insuranceDetails;
    }

    @Override
    public void addPerformanceRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.performanceRating = rating;
    }

    @Override
    public String getPerformanceSummary() {
        return "Performance Rating : " + performanceRating;
    }
}

class PartTimeEmployee extends Employee implements PerformanceEvaluation {
    private double hourlyRate;
    private int hoursWorked;
    private int performanceRating;

    public PartTimeEmployee(String name, int id, double baseSalary, double hourlyRate, int hoursWorked, int performanceRating) {
        super(name, id, baseSalary);
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        if (hoursWorked < 0) {
            throw new IllegalArgumentException("Hours worked cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        addPerformanceRating(performanceRating);
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public void trackWorkHours() {
        Logger logger = LogManager.getLogger();
        logger.debug("Part-time employee tracks hours dynamically.");
    }

    @Override
    public void addPerformanceRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.performanceRating = rating;
    }

    @Override
    public String getPerformanceSummary() {
        return "Performance Rating: " + performanceRating;
    }
}

class ContractEmployee extends Employee {
    private double projectFee;

    public ContractEmployee(String name, int id, double baseSalary, double projectFee) {
        super(name, id, baseSalary);
        if (projectFee < 0) {
            throw new IllegalArgumentException("Project fee cannot be negative.");
        }
        this.projectFee = projectFee;
    }

    @Override
    public double calculateSalary() {
        return projectFee;
    }

    @Override
    public void trackWorkHours() {
        Logger logger = LogManager.getLogger();
        logger.debug("Contract employee's hours are tracked based on project completion.");
    }
}

class Freelancer extends Employee implements Benefits, PerformanceEvaluation {
    private double taskRate;
    private int numberOfTasks;
    private int performanceRating;

    public Freelancer(String name, int id, double baseSalary, double taskRate, int numberOfTasks) {
        super(name, id, baseSalary);
        if (taskRate < 0) {
            throw new IllegalArgumentException("Task rate cannot be negative.");
        }
        if (numberOfTasks < 0) {
            throw new IllegalArgumentException("Number of tasks cannot be negative.");
        }
        this.taskRate = taskRate;
        this.numberOfTasks = numberOfTasks;
    }

    @Override
    public double calculateSalary() {
        return taskRate * numberOfTasks;
    }

    @Override
    public void trackWorkHours() {
        Logger logger = LogManager.getLogger();
        logger.debug("Freelancer tracks hours/task completion.");
    }

    @Override
    public double calculateBonus() {
        return 10000; // Fixed bonus assumed
    }

    @Override
    public String getInsuranceDetails() {
        return "Minimal freelancer insurance benefits.";
    }

    @Override
    public void addPerformanceRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.performanceRating = rating;
    }

    @Override
    public String getPerformanceSummary() {
        return "Performance Rating: " + performanceRating;
    }
}

class EmployeeManagementSystem {
    private static final Logger logger = LogManager.getLogger();
    private List<Employee> employees;

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee emp) {
        if (emp == null) {
            logger.error("Cannot add a null employee.");
            return;
        }
        employees.add(emp);
    }

    public void removeEmployee(int id) {
        boolean removed = employees.removeIf(emp -> emp.id == id);
        if (!removed) {
            logger.warn("No employee found with ID: " + id);
        }
    }

    public Employee findEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.id == id) {
                return emp;
            }
        }
        logger.warn("Employee with ID " + id + " not found.");
        return null;
    }

    public void generateMonthlyReports() {
        for (Employee emp : employees) {
            try {
                logger.debug(emp.getEmployeeDetails() + " Salary : " + emp.calculateSalary());
                if (emp instanceof PerformanceEvaluation) {
                    logger.debug(((PerformanceEvaluation) emp).getPerformanceSummary());
                }
                if (emp instanceof Benefits) {
                    logger.debug(((Benefits) emp).calculateBonus());
                    logger.debug(((Benefits) emp).getInsuranceDetails());
                }
            } catch (Exception e) {
                logger.error("Error generating report for employee ID: " + emp.id, e);
            }
        }
    }
}

public class AdminDashboard {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger();

        try {
            EmployeeManagementSystem ems = new EmployeeManagementSystem();

            // Create employees
            FullTimeEmployee ftEmp = new FullTimeEmployee("Alice", 1, 50000, 5000, "Health Insurance");
            PartTimeEmployee ptEmp = new PartTimeEmployee("Bob", 2, 20000, 60, 20, 4);
            ContractEmployee ctEmp = new ContractEmployee("Charlie", 3, 0, 10000);
            Freelancer freelancer = new Freelancer("Dave", 4, 0, 20, 150);

            // Add employees to the system
            ems.addEmployee(ftEmp);
            ems.addEmployee(ptEmp);
            ems.addEmployee(ctEmp);
            ems.addEmployee(freelancer);

            // Add performance ratings
            ftEmp.addPerformanceRating(5);
            ptEmp.addPerformanceRating(4);
            freelancer.addPerformanceRating(4);

            // Generate monthly reports
            ems.generateMonthlyReports();
        } catch (Exception e) {
            logger.error("Unexpected error in AdminDashboard.", e);
        }
    }
}
