package lk.payhere.androidsdk;

import java.io.Serializable;

/**
 * Created by chamika on 9/19/16.
 */

public class PHResponse<T> implements Serializable{

    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR_UNKNOWN = -1;
    public static final int STATUS_ERROR_DATA = -2;
    public static final int STATUS_ERROR_VALIDATION = -3;
    public static final int STATUS_ERROR_NETWORK = -4;
    public static final int STATUS_ERROR_PAYMENT = -5;
    public static final int STATUS_ERROR_CANCELED = -6;
    public static final int STATUS_SESSION_TIME_OUT = -7;


    private int status;
    private String message;
    private T data;

    public PHResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public PHResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess(){
        return status == STATUS_SUCCESS;
    }

    @Override
    public String toString() {
        return "PHResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }
}
