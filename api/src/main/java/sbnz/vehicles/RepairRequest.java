package sbnz.vehicles;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.time.LocalDateTime;

public class RepairRequest extends PanacheMongoEntity {

    public String userId;
    public EV vehicle;
    public RepairShop shop;
    public Problem.Type problem;
    public boolean accepted;
    public boolean rejected;
    public LocalDateTime scheduledAt;
    public Solution solution = new Solution();
    public Part.QuantityAlarm alarm;

    public boolean completed() {
        return this.solution != null;
    }

    public RepairRequest() {}

    public RepairRequest(String userId, EV vehicle, RepairShop shop, Problem.Type problem) {
        this.userId = userId;
        this.vehicle = vehicle;
        this.shop = shop;
        this.problem = problem;
    }

    public void accept(LocalDateTime scheduledAt) {
        this.accepted = true;
        this.scheduledAt = scheduledAt;
    }

    public void reject() {
        this.rejected = true;
    }

    public void complete(Solution solution) {
        solution.price = this.solution.price;
        this.solution = solution;
        this.solution.price = this.solution.price * 10 + solution.part.price;
        this.update();
    }
}
