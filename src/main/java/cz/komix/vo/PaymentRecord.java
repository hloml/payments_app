package cz.komix.vo;

/**
 * Created by hlom on 23.06.2017.
 */
public class PaymentRecord {
    private long amount;

    public PaymentRecord(Long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
