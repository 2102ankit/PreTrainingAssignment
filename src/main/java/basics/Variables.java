package basics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Variables {
    static final Logger logger = LogManager.getLogger();
    static final Scanner sc = new Scanner(System.in);

    //instance variable
    public String name;
    private double salary;

    // DEPARTMENT is a constant
    public static final String DEPARTMENT = "Development ";

    //constructor
    public Variables(String name){
        this.name = name;
    }

    public void printDetails(){
        logger.debug("name : {}",name);
        logger.debug("salary : {}", salary);
    }

    static void printAge(){
        //local variable scope
        int age = 0;
        age += 7;
        logger.debug(age);
    }

    public static void main(String[] args) {
        printAge();
        Variables object = new Variables("Ankit");
        object.printDetails();
    }
}
