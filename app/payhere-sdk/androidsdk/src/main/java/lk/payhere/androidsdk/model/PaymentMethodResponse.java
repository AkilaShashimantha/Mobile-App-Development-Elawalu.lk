package lk.payhere.androidsdk.model;

import java.util.HashMap;

public class PaymentMethodResponse {

    private int status;
    private String msg;
    private HashMap<String, Data> data;

    public class Data {
        private String imageUrl;
        private Size viewSize;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Size getViewSize() {
            return viewSize;
        }

        public void setViewSize(Size viewSize) {
            this.viewSize = viewSize;
        }
    }

    public class Size {
        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HashMap<String, Data> getData() {
        return data;
    }

    public void setData(HashMap<String, Data> data) {
        this.data = data;
    }
}
