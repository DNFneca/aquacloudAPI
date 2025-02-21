package cloud.aquacloud.aquacloudapi.type;

import lombok.Data;

@Data
public class PaymentInfoRequest {
    private int amount;
    private String currency;
    private String receiptEmail;

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }
}