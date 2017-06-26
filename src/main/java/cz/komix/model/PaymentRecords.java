package cz.komix.model;

import cz.komix.service.CurrencyConverter;
import cz.komix.vo.PaymentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/**
 * Model for storing payment records and providing methods for working with them
 */
public class PaymentRecords {

    // payment records
    private Map<String, PaymentRecord> records;

    private CurrencyConverter converter;

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentRecords.class);

    public PaymentRecords(Map<String, PaymentRecord> records, CurrencyConverter currencyConverter) {
        this.records = records;
        this.converter = currencyConverter;
    }

    /**
     * Get payment record by currency
     * @param key
     * @return
     */
    public PaymentRecord getRecord(String key) {
        return this.records.get(key);
    }

    /**
     * Create new record for currency
     * @param key
     * @param amount
     * @return if creation was successful
     */
    public boolean createNewRecord(String key, long amount) {
        logger.info("Creating new record for currency: " + key);
        PaymentRecord actualRecord = records.putIfAbsent(key, new PaymentRecord(amount));
        return actualRecord == null ? true: false;
    }

    /**
     * Update existing record for currency
     * @param key
     * @param prevRecord
     * @param amount
     * @return - if replacement was successful
     */
    public boolean updateRecord(String key, PaymentRecord prevRecord, long amount) {
        logger.info("Updating record for currency: " + key);
        if (prevRecord == null) {
            return false;
        }
        return records.replace(key, prevRecord, new PaymentRecord( amount));
    }

    /**
     * Print records for all currency(except for currency with amount 0) with their amount
     */
    public void printRecords() {
        logger.info("Printing all records:");
        records.forEach((k,v)-> {
            if (v.getAmount() != 0) {
                System.out.println(k + " " + v.getAmount() + " " + converter.getValue(k, v.getAmount()));
            }}
        );
    }
}
