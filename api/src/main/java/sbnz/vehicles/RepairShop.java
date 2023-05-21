package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.List;

public class RepairShop extends PanacheMongoEntity {

    public String name;
    public String address;
    public List<String> brands;
    public List<Part> parts;

    public RepairShop() {}

    public RepairShop(String name, String address, List<String> brands) {
        this.name = name;
        this.address = address;
        this.brands = brands;
    }

}
