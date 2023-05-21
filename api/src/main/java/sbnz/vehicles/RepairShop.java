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
        this.parts = brands.stream().flatMap(b -> Part.getAllParts(b).stream()).toList();
    }

    public void setAlarmQuantity(String partType, int quantity) {
        for (Part part : parts) {
            if (part.type.equals(partType)) {
                part.alarmQuantity = quantity;
                break;
            }
        }
        this.update();
    }

    public void reduceQuantity(String partType) {
        for (Part part : parts) {
            if (part.type.equals(partType)) {
                part.quantity -= 1;
                break;
            }
        }
        this.update();
    }

}
