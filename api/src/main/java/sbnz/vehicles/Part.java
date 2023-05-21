package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Part extends PanacheMongoEntity {

    public String make;
    public String name;

    public Part() {}

    public Part(String make, String name) {
        this.make = make;
        this.name = name;
    }

}
