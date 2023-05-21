package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.List;

public class Part extends PanacheMongoEntity {

    public String make;
    public String type;
    public String name;
    public double price;
    public int quantity;
    public int alarmQuantity;

    public Part() {}

    public Part(String make, String type, String name, double price, int quantity) {
        this.make = make;
        this.type = type;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.alarmQuantity = quantity;
    }

    public static Part battery(String make) {
         return new Part(make, "battery", "Battery", 10000, 1);
    }

    public static Part electricMotor(String make) {
         return new Part(make, "electric-motor", "Electric motor", 5000, 1);
    }

    public static Part batteryController(String make) {
        return new Part(make, "battery-controller", "Battery Controller", 1000, 1);
    }

    public static Part dcAcInverter(String make) {
        return new Part(make, "dc-ac-inverter", "DC-AC Inverter", 1000, 1);
    }

    public static Part pwmController(String make) {
        return new Part(make, "pwm-controller", "PWM Controller", 1000, 1);
    }

    public static Part coolingSystem(String make) {
        return new Part(make, "cooling-system", "Cooling System", 1000, 1);
    }

    public static Part engineBearings(String make) {
        return new Part(make, "engine-bearings", "Engine Bearings", 1000, 1);
    }

    public static Part gearbox(String make) {
        return new Part(make, "gearbox", "Gearbox", 1000, 1);
    }

    public static Part suspension(String make) {
        return new Part(make, "suspension", "Suspension", 1000, 1);
    }

    public static Part brakes(String make) {
        return new Part(make, "brakes", "Brakes", 1000, 1);
    }

    public static Part wheels(String make) {
        return new Part(make, "wheels", "Wheels", 1000, 1);
    }

    public static List<Part> getAllParts(String make) {
        return List.of(
            battery(make),
            electricMotor(make),
            batteryController(make),
            dcAcInverter(make),
            gearbox(make),
            suspension(make),
            brakes(make),
            wheels(make)
        );
    }

}
