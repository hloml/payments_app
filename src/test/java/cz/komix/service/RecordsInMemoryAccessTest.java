package cz.komix.service;

import cz.komix.model.PaymentRecords;
import cz.komix.vo.PaymentRecord;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;


public class RecordsInMemoryAccessTest {

    private RecordsInMemoryAccess recordsAccess;

    @Mock
    private PaymentRecords paymentRecords;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        recordsAccess = new RecordsInMemoryAccess(paymentRecords);

    }

    private PaymentRecord defaultPaymentRecord() {
        return new PaymentRecord(1000L);
    }

    @Test
    public void shouldPutRecordForNotExistingKey() {
        given(paymentRecords.getRecord("USD")).willReturn(null);
        given(paymentRecords.createNewRecord("USD", 1000)).willReturn(true);

        recordsAccess.putRecord("USD", 1000);
        verify(paymentRecords).getRecord("USD");
        verify(paymentRecords).createNewRecord("USD", 1000);
    }

    @Test
    public void shouldPutRecordForExistingKey() {
        PaymentRecord record = defaultPaymentRecord();
        given(paymentRecords.getRecord("USD")).willReturn(record);
        given(paymentRecords.updateRecord("USD", record, record.getAmount() + 1000)).willReturn(true);

        recordsAccess.putRecord("USD", 1000);
        verify(paymentRecords).updateRecord("USD", record, record.getAmount() + 1000);
    }


    @Test
    public void shouldPutAgainRecordForExistingKeyOnFirstFailure() {
        PaymentRecord record = defaultPaymentRecord();
        given(paymentRecords.getRecord("USD")).willReturn(record);
        given(paymentRecords.updateRecord("USD", record, record.getAmount() + 1000)).willReturn(false, true);

        recordsAccess.putRecord("USD", 1000);
        verify(paymentRecords, atLeast(2)).updateRecord("USD", record, record.getAmount() + 1000);
    }
}
