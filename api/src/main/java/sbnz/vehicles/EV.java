package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class EV extends PanacheMongoEntity {

    public String make;
    public String model;
    public int year;
    public String registration;

    public EV(String make, String model, int year, String registration) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.registration = registration;
    }

}
