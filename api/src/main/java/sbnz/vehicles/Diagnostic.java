package sbnz.vehicles;

public class Diagnostic {

    public record Measurement(String type, String unit, String message) {}

    public Measurement currentMeasurement;

    public void requestMeasurement(String unit, String message) {
        System.out.println("Requesting measurement: " + message + " in " + unit);
        var type = unit.equals("yes/no") ? "boolean" : "number";
        this.currentMeasurement = new Measurement(type, unit, message);
    }
}
