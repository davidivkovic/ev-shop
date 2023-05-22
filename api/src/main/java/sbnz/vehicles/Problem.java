package sbnz.vehicles;

import java.util.List;

public class Problem {

    public enum Types {
        ACCELERATION, TOP_SPEED, BRAKES, STEERING, SUSPENSION, ELECTRONICS, BATTERY, CHARGING, AIR_CONDITIONING, OTHER
    }

    public record Type(Types type, String name, String description) { }

    public Types type;

    public Problem(Types type) {
        this.type = type;
    }

    public static final List<Type> types = List.of(
        new Type(Types.CHARGING, "Charging", "The car has a problem with the battery and is unable to charge"),
        new Type(Types.BRAKES, "Brakes", "The car has a problem with the brakes and requires a longer stopping distance"),
        new Type(Types.ACCELERATION, "Acceleration", "The car slow on acceleration or has a sluggish response"),
        new Type(Types.TOP_SPEED, "Top speed", "The car has a low top speed or is unable to reach it"),
        new Type(Types.STEERING, "Steering", "The car has a problem with the steering and is difficult to steer"),
        new Type(Types.SUSPENSION, "Suspension", "The car has a problem with the suspension and is stiff or difficult to drive"),
        new Type(Types.ELECTRONICS, "Electronics", "The car has a problem with the electronics and the dashboard is showing errors"),
        new Type(Types.AIR_CONDITIONING, "Air conditioning", "The car has a problem with the air conditioning and is unable to cool or heat"),
        new Type(Types.OTHER, "Other", "The car has a problem with something else")
    );

    public static Type getType(Types type) {
        return types.stream().filter(t -> t.type.equals(type)).findFirst().orElse(null);
    }

}
