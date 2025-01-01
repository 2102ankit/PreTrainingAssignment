package multithreading;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

class ScrapedData{
    private String title;
    private List<String> links;

    public ScrapedData(String title, List<String> links) {
        this.title = title;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLinks() {
        return links;
    }
}

public class Scraper {
    // Executor Service for managing threads dynamically
    private static final Logger logger = LogManager.getLogger();

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static void scrapePageWithRetry(String url, ConcurrentMap<String, ScrapedData> results, int numberOfRetries){
        while (numberOfRetries>0){
            try {
                logger.debug("Attempting to scrape URL: " + url);
                Document doc = Jsoup.connect(url).get();

                String title = doc.title();
                Elements links = doc.select("a[href]");
                List<String> linksList = links.eachAttr("href");

                results.put(url, new ScrapedData(title,linksList));
                return;

            }catch (IOException e){
                numberOfRetries--;
                logger.error("Retrying " + url + " (" + numberOfRetries + " retries left)");
            }
        }
    }

    public static void main(String[] args) {
        List<String> urlsToScrape = List.of(
                "https://en.wikipedia.org/wiki/World_War_II",
                "https://en.wikipedia.org/wiki/Industrial_Revolution",
                "https://en.wikipedia.org/wiki/French_Revolution",
                "https://en.wikipedia.org/wiki/Mughal_Empire",
                "https://en.wikipedia.org/wiki/Ancient_Egypt",
                "https://en.wikipedia.org/wiki/Renaissance",
                "https://en.wikipedia.org/wiki/American_Civil_War",
                "https://en.wikipedia.org/wiki/Partition_of_India"
        );

        ConcurrentMap<String, ScrapedData> results = new ConcurrentHashMap<>();

        List<Future<Void>> futures = new CopyOnWriteArrayList<>();

        for(String url : urlsToScrape){
            Callable<Void> task = () -> {
                scrapePageWithRetry(url, results, 3); //retry 3 timex max
                return null;
            };

            Future<Void> future = executor.submit(task);
            futures.add(future);
        }

        for(Future<Void> future : futures){
            try {
                future.get();
            }catch (InterruptedException | ExecutionException e){
                logger.error("Error in executing task : " + e.getMessage());
            }
        }

        executor.shutdown();
        try {
            long startTime = System.currentTimeMillis();
            long timeout = 5*60*1000; //5 mins in millisecs;
            boolean isTerminated = false;

            while (!isTerminated){
                isTerminated = executor.isTerminated();
                if(!isTerminated){
                    long timeElapsed = System.currentTimeMillis() - startTime;
                    if(timeElapsed>timeout){
                        logger.warn("Timeout reached not all tasks completed");
                        break;
                    }
                    Thread.sleep(1000);
                }
            }
        }catch (InterruptedException e){
            logger.error("Executor Termination Interrupted : "+e.getMessage());
        }finally {
            executor.shutdownNow();
        }

        //Display results

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("logs/results.txt",true))){
            results.forEach((url,data)->{
                try {
                    writer.write("URL : "+url);
                    writer.newLine();
                    writer.write("Title: "+data.getTitle());
                    writer.newLine();
                    writer.write("Links : "+data.getLinks());
                    writer.newLine();
                    writer.write("-------------------------");
                    writer.newLine();
                }catch (IOException e) {
                    logger.error("Error writing to file: " + e.getMessage());
                }
            });
        }catch (IOException e){
            logger.error("Error opening/closing the file: " + e.getMessage());
        }

    }
}
