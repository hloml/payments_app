package cz.komix.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * Converter loading exchange rates from properties file. Provides method for exchange value for currency to USD.
 */
public class CurrencyConverter {

    HashMap<String, Double> convertRates;

    public CurrencyConverter(HashMap<String, Double> convertRates) throws IOException {
        this.convertRates = convertRates;
        loadRatesFromProperties();
    }


    /**
     * Load currency and their exchange rates to map
     * @throws IOException
     */
    public void loadRatesFromProperties() throws IOException {
        Properties MyPropertyFile = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

        MyPropertyFile.load(inputStream);

        Set<Object> keys = MyPropertyFile.keySet();

        for (Object k : keys) {
            String key = (String) k;
            Double value = Double.parseDouble(MyPropertyFile.getProperty(key));
            convertRates.put(key, value);
        }
    }

    /**
     * Convert value for currency to USD. If currency has no exchange rate or is USD, then return empty String.
     * @param currency
     * @param amount
     * @return
     */
    public String getValue(String currency, Long amount) {
        Double convertRate = convertRates.get(currency);
        if (convertRate == null || currency.equals("USD")) {
            return "";
        }
        return "(USD " + amount * convertRate + ")";
    }
}
