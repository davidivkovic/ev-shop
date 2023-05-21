package sbnz.api;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import sbnz.vehicles.EV;
import sbnz.vehicles.Problem;
import sbnz.vehicles.RepairRequest;
import sbnz.vehicles.RepairShop;
import sbnz.users.User;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
public class Vehicles extends Resource {

    @POST
    @Authenticated
    @Path("/add")
    public Response addCar(EV vehicle) {
        var user = User.findById(userId());
        if (user == null) return badRequest("User not found.");

        vehicle.persist();
        user.vehicle = vehicle;
        user.update();

        return ok();
    }

    @POST
    @Authenticated
    @Path("/request-repair")
    public Response requestRepair(
            @QueryParam("shop") String shopId,
            @QueryParam("problem") Problem.Types problemType
    ) {
        var user = User.findById(userId());
        if (user == null) return badRequest("User not found.");

        RepairShop shop = RepairShop.findById(new ObjectId(shopId));
        if (shop == null) return badRequest("Shop not found.");

        var problem = Problem.getType(problemType);
        if (problem == null) return badRequest("Problem not found.");

        var request = new RepairRequest(user.id.toString(), user.vehicle, shop, problem);
        request.persist();

        return ok();
    }

}
