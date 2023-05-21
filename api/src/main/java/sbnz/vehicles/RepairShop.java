package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.ArrayList;
import java.util.List;

public class RepairShop extends PanacheMongoEntity {

    public String name;
    public String address;
    public List<String> brands;
    public List<Part> parts;
    public List<String> partQuantityAlarmRules = new ArrayList<>();

    public List<Part> getParts() {
        return this.parts;
    }

    public RepairShop() {}

    public RepairShop(String name, String address, List<String> brands) {
        this.name = name;
        this.address = address;
        this.brands = brands;
    }

    public void setAlarmQuantity(String partMake, String partType, int quantity) {
        for (Part part : parts) {
            if (part.type.equals(partType) && part.make.equals(partMake)) {
                part.alarmQuantity = quantity;
                break;
            }
        }
        partQuantityAlarmRules
            .stream()
            .filter(rule -> rule.contains("part-quantity-alarm_" + partMake  + "-" + partType))
            .findFirst()
            .ifPresent(rule -> {
                partQuantityAlarmRules.remove(rule);
            });
    }

    public void reduceQuantity(String partMake, String partType) {
        for (Part part : parts) {
            if (part.type.equals(partType) && part.make.equals(partMake)) {
                part.quantity -= 1;
                break;
            }
        }
        this.update();
    }

}
