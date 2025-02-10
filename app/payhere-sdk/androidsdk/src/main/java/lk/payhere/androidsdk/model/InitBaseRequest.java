package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InitBaseRequest implements Serializable {

    private String merchantId;
    //Deprecated
    private String merchantSecret;

    private Customer customer;

    private String orderId;
    private String itemsDescription;

    private boolean internal = false;

    private String custom1;
    private String custom2;

    private String notifyUrl;
    private String cancelUrl;
    private String returnUrl;

    private List<Item> items;

    private double amount;
    private String currency;
    private boolean isHoldOnCardEnabled;

    private boolean isSandBox;

    public boolean isSandBox() {
        return isSandBox;
    }

    public void setSandBox(boolean sandBox) {
        isSandBox = sandBox;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    //Deprecated
    public String getMerchantSecret() {
        return merchantSecret;
    }

    //Deprecated
    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }

    public Customer getCustomer() {
        if (customer == null) {
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemsDescription() {
        return itemsDescription;
    }

    public void setItemsDescription(String itemsDescription) {
        this.itemsDescription = itemsDescription;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public List<Item> getItems() {
        if(items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isHoldOnCardEnabled() {
        return isHoldOnCardEnabled;
    }

    public void setHoldOnCardEnabled(boolean holdOnCardEnabled) {
        isHoldOnCardEnabled = holdOnCardEnabled;
    }
}
