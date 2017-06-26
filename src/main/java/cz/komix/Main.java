package cz.komix;

import cz.komix.model.PaymentRecords;
import cz.komix.service.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main class for starting reading input and printer thread
 */
public class Main {

    public static void main(String[] args) throws IOException {
        CurrencyConverter converter = new CurrencyConverter(new HashMap());

        PaymentRecords paymentRecords = new PaymentRecords(new ConcurrentHashMap<>(), converter);
        RecordsAccess recordsAccess = new RecordsInMemoryAccess(paymentRecords);

        System.out.println("Starting sheduled printer ");
        CurrencySheduledPrinter printer = new CurrencySheduledPrinter(recordsAccess);
        Thread printerThread = new Thread(printer);
        printerThread.start();

        System.out.println("Starting reading for user input");
        UserInputService inputService = new UserInputService(recordsAccess);
        inputService.startReadingInput();
        printerThread.interrupt();
        System.out.println("Exiting application");

    }
}
