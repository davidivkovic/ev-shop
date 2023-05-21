package sbnz.api;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.*;
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

        shop.persist();
        user.update();

        return ok();
    }

    @GET
    @Path("/")
    public Response getShops(@QueryParam("brand") String brand) {
        var shops = RepairShop.list("brands = ?1", brand);
        return ok(shops);
    }

    @GET
    @Path("/{id}/brands")
    public Response getBrands(@PathParam("id") String id) {
        RepairShop shop = RepairShop.findById(id);
        if (shop == null) return badRequest("Shop not found.");

        return ok(shop.brands);
    }

    @GET
    @Path("/{id}/parts")
    public Response getParts(@PathParam("id") String id) {
        RepairShop shop = RepairShop.findById(id);
        if (shop == null) return badRequest("Shop not found.");

        return ok(shop.parts);
    }

    @POST
    @Path("/{id}/parts/alarm-quantity")
    public Response setAlarmQuantity(
            @PathParam("id") String id,
            @QueryParam("part") String partType,
            @QueryParam("quantity") int quantity
    ) {
        RepairShop shop = RepairShop.findById(id);
        if (shop == null) return badRequest("Shop not found.");

        shop.setAlarmQuantity(partType, quantity);
        shop.update();

        return ok();
    }

}
