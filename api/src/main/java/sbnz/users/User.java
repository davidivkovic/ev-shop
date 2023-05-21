package sbnz.users;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.smallrye.jwt.build.Jwt;

import org.bson.types.ObjectId;
import sbnz.vehicles.EV;
import sbnz.vehicles.RepairShop;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

public class User extends PanacheMongoEntity {

    public static class Roles {

        public static final String CUSTOMER = "customer";
        public static final String REPAIRMAN = "repairman";
    }

    public String email;
    public String password;
    public String role;
    public String firstName;
    public String lastName;
    public String fullName;
    public EV vehicle;
    public RepairShop shop;

    public static User register(String firstName, String lastName, String role, String email, String password) {
        User user = new User();

        user.password = password;
        user.firstName = firstName;
        user.lastName = lastName;
        user.fullName = firstName + " " + lastName;
        user.email = email;
        user.role = role;

        user.persist();

        return user;
    }


    public static boolean authenticate(User user, String password) {
        return user != null && user.password.equals(password);
    }

    public String generateToken() {
        return Jwt
                .issuer("http://localhost:8080/auth")
                .audience("http://localhost:5173")
                .upn(this.id.toString())
                .subject(this.email)
                .groups(new HashSet<>(List.of(this.role)))
                .expiresIn(Duration.ofDays(90))
                .sign();
    }

    public static User findById(String id) {
        return find("_id", new ObjectId(id)).firstResult();
    }

    public static User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public boolean isRole(String role) {
        return this.role.equals(role);
    }

}
