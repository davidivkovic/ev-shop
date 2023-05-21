package sbnz.api;

import sbnz.api.dto.repair_requests.RepairRequestDTO;
import sbnz.vehicles.RepairRequest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;

@Path("/repair-requests")
@Produces(MediaType.APPLICATION_JSON)
public class RepairRequests extends Resource {

    @POST
    @Path("/{id}")
    public Response update(@PathParam("id") String id, RepairRequestDTO dto) {
        RepairRequest request = RepairRequest.findById(id);
        if (request == null) return badRequest("Request not found.");

        if (dto.accepted) request.accept(dto.scheduledAt);
        else request.reject();

        request.update();

        return ok();
    }

    @GET
    @Path("/")
    public Response getAll() {
        var requests = RepairRequest.listAll();
        return ok(requests);
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        var request = RepairRequest.findById(id);
        if (request == null) return badRequest("Request not found.");

        return ok(request);
    }

}
