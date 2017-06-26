package cz.komix.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sheduled Printer for defined interval
 */
public class CurrencySheduledPrinter implements Runnable {

    private static final int INTERVAL = 60000;
    private boolean finished = false;

    private RecordsAccess recordsAccess;

    private static final Logger logger =
            LoggerFactory.getLogger(CurrencySheduledPrinter.class);



    public CurrencySheduledPrinter(RecordsAccess recordsAccess) {
        this.recordsAccess = recordsAccess;
    }


    /**
     * Print records for currency in defined interval
     */
    @Override
    public void run() {
        while (!finished) {
            logger.info("Printing thread scheduled");
            try {
                recordsAccess.printRecords();
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                logger.info("Printing thread interupted: " + e);
                return;
            }
        }
    }
}
