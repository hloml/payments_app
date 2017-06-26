package cz.komix.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;

public class CurrencyConverterTest {

    private CurrencyConverter converter;

    private HashMap<String, Double> convertRates;

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        initPropertiesRater();
        converter = new CurrencyConverter(convertRates);
    }

    private void initPropertiesRater() {
        convertRates = new HashMap<>();
        convertRates.put("RMB", 0.146239);
    }

    @Test
    public void shouldConvertToUSD() {
        String value = converter.getValue("RMB", 2000L);
        Assert.assertEquals("(USD 292.478)", value);
    }

    @Test
    public void shouldReturnEmptyStringForNotExistingKey() {
        String value = converter.getValue("NOT_EXISTING", 2000L);
        Assert.assertEquals("", value);
    }

    @Test
    public void shouldNotConvertForUSD() {
        String value = converter.getValue("USD", 2000L);
        Assert.assertEquals("", value);
    }

}
