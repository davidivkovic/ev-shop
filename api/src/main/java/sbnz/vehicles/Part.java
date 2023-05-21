package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Part extends PanacheMongoEntity {

    public String make;
    public String name;
    public double price;
    public int quantity;

    public Part() {}

    public Part(String make, String name, double price, int quantity) {
        this.make = make;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}
