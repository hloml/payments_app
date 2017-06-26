package cz.komix.model;

import cz.komix.service.CurrencyConverter;
import cz.komix.vo.PaymentRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;

public class PaymentRecordsTest {

    private PaymentRecords paymentRecords;

    @Mock
    private CurrencyConverter converter;

    private long dolarsInitAmount = 1000;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.paymentRecords = new PaymentRecords(new ConcurrentHashMap<>(), converter);
    }


    private void initPaymentRecords() {
        paymentRecords.createNewRecord("USD", dolarsInitAmount);
        paymentRecords.createNewRecord("HKD", 100);
        paymentRecords.createNewRecord("RMB", 2000);
    }

    @Test
    public void shouldGetRecord() {
        initPaymentRecords();
        PaymentRecord record = paymentRecords.getRecord("USD");
        Assert.assertEquals(dolarsInitAmount, record.getAmount());
    }

    @Test
    public void shouldNotGetRecord() {
        initPaymentRecords();
        PaymentRecord record = paymentRecords.getRecord("NotExisting");
        Assert.assertEquals(null, record);
    }

    @Test
    public void shouldCreateNewRecord() {
        boolean success = paymentRecords.createNewRecord("USD", 5000);
        PaymentRecord record = paymentRecords.getRecord("USD");
        Assert.assertEquals(5000, record.getAmount());
        Assert.assertEquals(true, success);
    }

    @Test
    public void shouldntCreateNewRecordIfExistForKey() {
        initPaymentRecords();
        boolean success = paymentRecords.createNewRecord("USD", 5000);
        PaymentRecord record = paymentRecords.getRecord("USD");
        Assert.assertEquals(1000, record.getAmount());
        Assert.assertEquals(false, success);
    }

    @Test
    public void shouldUpdateRecordPlusDebit() {
        initPaymentRecords();
        PaymentRecord record = paymentRecords.getRecord("USD");
        boolean success = paymentRecords.updateRecord("USD", record,record.getAmount() + 5000);
        record = paymentRecords.getRecord("USD");
        Assert.assertEquals(dolarsInitAmount + 5000, record.getAmount());
        Assert.assertEquals(true, success);
    }


    @Test
    public void shouldUpdateRecordMinusDebit() {
        initPaymentRecords();
        PaymentRecord record = paymentRecords.getRecord("USD");
        boolean success = paymentRecords.updateRecord("USD", record,record.getAmount() + (-5000));
        record = paymentRecords.getRecord("USD");
        Assert.assertEquals(dolarsInitAmount - 5000, record.getAmount());
        Assert.assertEquals(true, success);
    }

    @Test
    public void shouldNotUpdateNotExistingRecord() {
        initPaymentRecords();
        PaymentRecord record = new PaymentRecord(5000L);
        boolean success = paymentRecords.updateRecord("NotExisting", record, 5000);
        record = paymentRecords.getRecord("NotExisting");
        Assert.assertEquals(null, record);
        Assert.assertEquals(false, success);
    }

    @Test
    public void shouldNotUpdateNullrecord() {
        boolean success = paymentRecords.updateRecord("NotExisting", null, 5000);
        Assert.assertEquals(false, success);
    }
}
