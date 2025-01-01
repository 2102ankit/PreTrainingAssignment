package oopswithexceptions;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;


public class Misc {
    static final Logger logger = LogManager.getLogger();

    String name;
    static int salary = 60000;
    static int userId = 1;

    //default constructor
    Misc(){
        logger.debug("Misc object created");
        salary*=2;
        userId++;
    }

    //parameterized constructor
    Misc(String name){
        this.name = name;
        salary*=2;
        userId++;
    }

    @Override
    public String toString() {
        return "Person{ name= " + this.name + ", userId = " + userId + ", salary = "+ salary + " }";
    }

    public static void main(String[] args) {
        Misc regularObject = new Misc();
        logger.debug(regularObject.toString());

        Misc namedObject = new Misc("Ankit");
        logger.debug(namedObject.toString());
    }

}
