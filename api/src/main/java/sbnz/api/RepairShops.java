package sbnz.api;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import sbnz.vehicles.Part;
import sbnz.vehicles.RepairShop;
import sbnz.users.User;
import org.drools.template.ObjectDataCompiler;

import java.io.InputStream;
import java.util.List;

@Path("/repair-shops")
@Produces(MediaType.APPLICATION_JSON)
public class RepairShops extends Resource {

    @POST
    @Authenticated
    @Path("/add")
    public Response addShop(RepairShop shop) {
        var user = User.findById(userId());
        if (user == null) return badRequest("User not found.");

        shop.parts = shop.brands.stream().flatMap(b -> Part.getAllParts(b).stream()).toList();
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
        RepairShop shop = RepairShop.findById(new ObjectId(id));
        if (shop == null) return badRequest("Shop not found.");

        return ok(shop.brands);
    }

    @GET
    @Path("/{id}/parts")
    public Response getParts(@PathParam("id") String id) {
        RepairShop shop = RepairShop.findById(new ObjectId(id));
        if (shop == null) return badRequest("Shop not found.");

        return ok(shop.parts);
    }

    @POST
    @Path("/{id}/parts/alarm-quantity")
    public Response setAlarmQuantity(
            @PathParam("id") String id,
            @QueryParam("make") String partMake,
            @QueryParam("part") String partType,
            @QueryParam("quantity") int quantity
    ) {
        RepairShop shop = RepairShop.findById(new ObjectId(id));
        if (shop == null) return badRequest("Shop not found.");

        shop.setAlarmQuantity(partMake, partType, quantity);

        try (InputStream template = RepairShops.class.getResourceAsStream("/rules/parts-quantity-template.drt")) {
            var converter = new DataProviderCompiler();
            var dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{ partMake, partType, String.valueOf(quantity) },
            });
            String drl = converter.compile(dataProvider, template);
            System.out.println(drl);
            shop.partQuantityAlarmRules.add(drl);
        }
        catch (Exception e) { return badRequest("Failed to set alarm quantity."); }

        shop.update();

        return ok();
    }

}
