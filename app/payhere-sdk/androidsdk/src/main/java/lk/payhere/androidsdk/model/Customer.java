package lk.payhere.androidsdk.model;

import java.io.Serializable;

/**
 * Created by chamika on 9/18/16.
 */

public class Customer implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;
    private Address deliveryAddress;

    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName Customer’s First Name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName Customer’s Last Name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    /**
     * @param email Customer’s Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * @param phone Customer’s Phone No
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    private void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return Delivery Address
     */
    public Address getDeliveryAddress() {
        if (deliveryAddress == null) {
            deliveryAddress = new Address();
        }
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
