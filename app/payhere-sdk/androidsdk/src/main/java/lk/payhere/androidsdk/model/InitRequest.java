package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InitRequest extends InitBaseRequest implements Serializable {

    //recurring payment
    private String recurrence;
    private String duration;
    private double startupFee;

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getStartupFee() {
        return startupFee;
    }

    public void setStartupFee(double startupFee) {
        this.startupFee = startupFee;
    }
}
