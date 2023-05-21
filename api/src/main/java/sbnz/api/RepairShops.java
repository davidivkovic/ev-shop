package sbnz.api;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sbnz.vehicles.RepairShop;
import sbnz.users.User;

@Path("/repair-shops")
@Produces(MediaType.APPLICATION_JSON)
public class RepairShops extends Resource {

    @POST
    @Authenticated
    @Path("/add")
    public Response addShop(RepairShop shop) {
        var user = User.findById(userId());
        if (user == null) return badRequest("User not found.");

        user.shop = shop;
        user.update();

        return ok();
    }

    @GET
    @Path("/")
    public Response getShops() {
        var shops = RepairShop.listAll();
        return ok(shops);
    }

}
