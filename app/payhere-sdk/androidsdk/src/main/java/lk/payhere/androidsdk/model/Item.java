package lk.payhere.androidsdk.model;

import java.io.Serializable;

/**
 * Created by chamika on 9/18/16.
 */

public class Item implements Serializable {

    private String id;
    private String name;
    private int quantity;
    private double amount;

    public Item() {
    }

    public Item(String id, String name, int quantity, double amount) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    /**
     * @param id Model number of Item
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name Name of Item
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity Quantity of Item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    /**
     * @param amount Item price in currency defined in request
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
