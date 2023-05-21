package sbnz.vehicles.events;

public class BatteryTemperatureAlarm {

    public String vehicleId;
    public double temperature;

    public BatteryTemperatureAlarm() {}

    public BatteryTemperatureAlarm(String vehicleId, double temperature) {
        this.vehicleId = vehicleId;
        this.temperature = temperature;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public double getTemperature() {
        return temperature;
    }

}
