package cz.komix.service;


public interface RecordsAccess {
    public void putRecord(String currency, long amount);
    public void printRecords();
}
