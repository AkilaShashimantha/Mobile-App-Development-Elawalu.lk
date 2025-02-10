package lk.payhere.androidsdk.model;

public class PaymentInitResult {

    private int status;
    private String msg;
    private Data data;

    public class Data {
        private Order order;
        private Business business;
        private String[] paymentMethods;
        private Redirection redirection;

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

        public String[] getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(String[] paymentMethods) {
            this.paymentMethods = paymentMethods;
        }

        public Redirection getRedirection() {
            return redirection;
        }

        public void setRedirection(Redirection redirection) {
            this.redirection = redirection;
        }
    }

    public class Order {
        private String orderKey;
        private long amount;
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

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
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

    public class Business {
        private String name;
        private String logo;

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
    }

    public class Redirection {
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
}
