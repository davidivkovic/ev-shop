package sbnz.vehicles.events;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;

@Role(Role.Type.EVENT)
@Expires("1h")
@Timestamp("timestamp")
public class BatteryTemperatureEvent {

    public String userId;
    public String vehicleId;
    public double temperature;
    public Date timestamp;

    public BatteryTemperatureEvent() {}

    public BatteryTemperatureEvent(String userId, String vehicleId, double temperature) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.temperature = temperature;
        this.timestamp = new Date();
    }

    public String getUserId() {
        return userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public double getTemperature() {
        return temperature;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
