package basics;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateTime {
    static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        //instantiate current date and time
        Date date = new Date();
        logger.debug(date);

        SimpleDateFormat american = new SimpleDateFormat("E, MM.dd.yyyy 'at' hh:mm:ss a z");
        logger.debug(american.format(date));

        //calculate time difference
        try {
            long start = new Date().getTime();

            Thread.sleep(1000000);

            long end = new Date().getTime();

            Date now = new Date(end-start);
            logger.debug(american.format(now));
        }catch (Exception e){
            logger.error(e);
        }

    }
}
