package sbnz.vehicles;

import io.quarkus.scheduler.Scheduler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import sbnz.vehicles.events.BatteryTemperatureAlarm;
import sbnz.vehicles.events.BatteryTemperatureEvent;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;

@ApplicationScoped
public class Sim {

    @Inject Scheduler scheduler;
    public KieSession kSession;
    Random random = new Random();

    public Sim() {
        kSession = createSession();
    }

    public KieSession createSession() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();
        return kc.newKieSession();
    }

    public void start(String userId, String vehicleId) {
        scheduler.newJob(BatteryTemperatureEvent.class.getName())
                .setInterval("1s")
                .setTask(ctx -> {
                    var temperature = random.nextInt(130, 150);
                    kSession.insert(new BatteryTemperatureEvent(userId, vehicleId, temperature));
                    System.out.println(
                            LocalDateTime.now() +
                            " - Battery temperature event" +
                            " - Vehicle: " + vehicleId +
                            " - Temperature: " + temperature
                    );
                    var rulesFired = kSession.fireAllRules();
                    if (rulesFired > 0) {
                        var alarm = getAlarm();
                        System.out.println(
                                LocalDateTime.now() +
                                " - Battery temperature alarm" +
                                " - Vehicle: " + vehicleId +
                                " - Temperature: " + temperature +
                                " - Alarm: " + alarm
                        );
                        stop();
                    }
                })
                .schedule();
    }

    public void stop() {
        scheduler.unscheduleJob(BatteryTemperatureEvent.class.getName());
    }

    public void reset() {
        this.stop();
        kSession.dispose();
        kSession = createSession();
    }

    @SuppressWarnings("unchecked")
    public BatteryTemperatureAlarm getAlarm() {
        var alarms = (Collection<BatteryTemperatureAlarm>) kSession.getObjects(
            object -> object.getClass().getName().equals(BatteryTemperatureAlarm.class.getName())
        );
        return alarms.stream().findFirst().orElse(new BatteryTemperatureAlarm());
    }

}
