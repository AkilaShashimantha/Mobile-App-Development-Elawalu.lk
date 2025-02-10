package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.List;

public class PaymentInitResponse implements Serializable {

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

        public String type;
        public Order order;
        public Business business;
        public List<PaymentMethod> paymentMethods;

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
        private String discountPercentage;
        private Submission submission;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getDiscountPercentage() {
            return discountPercentage;
        }

        public void setDiscountPercentage(String discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public Submission getSubmission() {
            return submission;
        }

        public void setSubmission(Submission submission) {
            this.submission = submission;
        }
    }

    public static class Submission implements Serializable{

        private String redirectType;
        private String url;

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
    }

}
