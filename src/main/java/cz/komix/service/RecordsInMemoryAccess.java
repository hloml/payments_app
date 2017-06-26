package cz.komix.service;

import cz.komix.model.PaymentRecords;
import cz.komix.vo.PaymentRecord;

/**
 * Service for working with payments records in memory
 */
public class RecordsInMemoryAccess implements RecordsAccess{

    private PaymentRecords paymentRecords;

    public RecordsInMemoryAccess(PaymentRecords paymentRecords) {
        this.paymentRecords = paymentRecords;
    }

    /**
     * Put record to payment records. If currency has no record, then create new. Else update existing.
     * If operation failed, then method will be called again until success.
     * @param currency
     * @param amount
     */
    @Override
    public void putRecord(String currency, long amount) {
        PaymentRecord record = paymentRecords.getRecord(currency);
        boolean putSucess;

        if (record == null) {
            putSucess = paymentRecords.createNewRecord(currency, amount);
        }
        else {
            putSucess = paymentRecords.updateRecord(currency, record,record.getAmount() + amount);
        }

        if (!putSucess) {
            onFailedPutRecord(currency, amount);
        }
    }

    /**
     * Print records
     */
    @Override
    public void printRecords() {
        paymentRecords.printRecords();
    }

    /**
     * When operation failed, then call method again
     * @param currency
     * @param amount
     */
    private void onFailedPutRecord(String currency, long amount) {
        putRecord(currency, amount);
    }
}
