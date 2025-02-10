package lk.payhere.androidsdk.model;

import java.io.Serializable;

/**
 * Created by chamika on 9/18/16.
 */

public class Address implements Serializable {
    private String address;
    private String city;
    private String country;

    public String getAddress() {
        return address;
    }

    /**
     * @param address Address Line1 + Line2
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    /**
     * @param city Customer’s City
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    /**
     * @param country Customer’s Country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
