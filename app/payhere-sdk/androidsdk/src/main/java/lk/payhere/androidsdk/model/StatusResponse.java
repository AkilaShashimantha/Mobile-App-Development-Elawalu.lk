package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chamika on 9/26/16.
 */

public class StatusResponse implements Serializable {

    private int status;
    private long paymentNo;
    private String currency;
    private long price;
    private String sign;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(long paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getCurrency() {
        return currency;
    }

    public long getPrice() {
        return price;
    }

    public String getSign() {
        return sign;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "status=" + status +
                ", paymentNo=" + paymentNo +
                '}';
    }

    public Status getStatusState() {
        return Status.getStatus(this.status);
    }

    public enum Status {
        INIT(0), PAYMENT(1), SUCCESS(2), HOLD(3), FAILED(-2), CANCELED(-6);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static Status getStatus(int value) {
            for (Status status : Status.values()) {
                if (status.value() == value) {
                    return status;
                }
            }
            return null;
        }
    }
}
