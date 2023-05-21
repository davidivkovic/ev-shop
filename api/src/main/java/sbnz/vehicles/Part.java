package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Part extends PanacheMongoEntity {

    public String make;
    public String name;

}
