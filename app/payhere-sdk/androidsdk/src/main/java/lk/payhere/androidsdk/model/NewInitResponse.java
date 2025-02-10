package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.List;

public class NewInitResponse implements Serializable {

    private int status;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }




    public static class Data implements Serializable{

        private String type;
        private Order order;
        private Business business;
        private List<PaymentMethod> paymentMethods;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public Business getBusiness() {
            return business;
        }

        public void setBusiness(Business business) {
            this.business = business;
        }

        public List<PaymentMethod> getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
            this.paymentMethods = paymentMethods;
        }
    }

    public static class Order implements Serializable{

        private String orderKey;
        private int amount;
        private String amountFormatted;
        private String currency;
        private String currencyFormatted;
        private String shortDescription;
        private String longDescription;


        public String getOrderKey() {
            return orderKey;
        }

        public void setOrderKey(String orderKey) {
            this.orderKey = orderKey;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAmountFormatted() {
            return amountFormatted;
        }

        public void setAmountFormatted(String amountFormatted) {
            this.amountFormatted = amountFormatted;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCurrencyFormatted() {
            return currencyFormatted;
        }

        public void setCurrencyFormatted(String currencyFormatted) {
            this.currencyFormatted = currencyFormatted;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }
    }

    public static class Business implements Serializable{

        private String name;
        private String logo;
        private String primaryColor;
        private String textColor;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getPrimaryColor() {
            return primaryColor;
        }

        public void setPrimaryColor(String primaryColor) {
            this.primaryColor = primaryColor;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
    }

    public static class PaymentMethod implements Serializable{

        private String method;
        private int orderNo;
        private String discount;
        private String submissionCode;
        private Submission submission;
        private View view;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getSubmissionCode() {
            return submissionCode;
        }

        public void setSubmissionCode(String submissionCode) {
            this.submissionCode = submissionCode;
        }

        public Submission getSubmission() {
            return submission;
        }

        public void setSubmission(Submission submission) {
            this.submission = submission;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

    public static class Submission implements Serializable{

        private String redirectType;
        private String url;
        private MobileUrls mobileUrls;

        public String getRedirectType() {
            return redirectType;
        }

        public void setRedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public MobileUrls getMobileUrls() {
            return mobileUrls;
        }

        public void setMobileUrls(MobileUrls mobileUrls) {
            this.mobileUrls = mobileUrls;
        }
    }

    public static class MobileUrls implements Serializable{
        private String IOS;

        public String getIOS() {
            return IOS;
        }

        public void setIOS(String IOS) {
            this.IOS = IOS;
        }
    }

    public static class View implements Serializable{

        private String imageUrl;
        private WindowSize windowSize;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public WindowSize getWindowSize() {
            return windowSize;
        }

        public void setWindowSize(WindowSize windowSize) {
            this.windowSize = windowSize;
        }
    }

    public static class WindowSize implements Serializable{

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
}
