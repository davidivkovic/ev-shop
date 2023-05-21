package sbnz.api;

import io.quarkus.security.Authenticated;
import org.bson.types.ObjectId;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import sbnz.api.dto.repair_requests.RepairRequestDTO;
import sbnz.vehicles.Diagnostic;
import sbnz.vehicles.Problem;
import sbnz.vehicles.RepairRequest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sbnz.vehicles.Solution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/repair-requests")
@Produces(MediaType.APPLICATION_JSON)
public class RepairRequests extends Resource {

    public record Session(RepairRequest request, KieSession kSession) { }

    public static Map<String, Session> sessions = new HashMap<>();

    @GET
    @Path("/problems")
    public Response getProblems() {
        return ok(Problem.types);
    }

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

    @POST
    @Path("/{id}/reset-diagnostics")
    @Authenticated
    public Response reset() {
        var user = userId();
        if (sessions.containsKey(user)) {
            sessions.get(user).kSession.dispose();
            sessions.remove(user);
        }
        return ok();
    }

    @POST
    @Path("/{id}/diagnostics")
    @Authenticated
    @SuppressWarnings("unchecked")
    public Response diagnose(
            @PathParam("id") String id,
            @QueryParam("measurement") String measurement
    ) {

        var user = userId();
        boolean initialSession = !sessions.containsKey(user);

        if (initialSession) {

            RepairRequest request = RepairRequest.findById(new ObjectId(id));
            if (request == null) return badRequest("Repair request not found.");

            KieServices ks = KieServices.Factory.get();
            KieContainer kc = ks.newKieClasspathContainer();
            KieSession kSession = kc.newKieSession();

            kSession.setGlobal("diagnostic", new Diagnostic());
            sessions.put(user, new Session(request, kSession));

            var problem = new Problem(Problem.Types.CHARGING);
            kSession.insert(problem);
        }

        var session = sessions.get(user);

        var kSession = session.kSession;
        var request = session.request;

        var diagnostic = (Diagnostic) session.kSession.getGlobal("diagnostic");
        diagnostic.currentMeasurement = null;

        FactHandle handle = null;

        if (!initialSession && measurement != null) {
            var value = (measurement.equals("yes") || measurement.equals("no"))
                    ? measurement
                    : Double.parseDouble(measurement);
            handle = kSession.insert(value);
        }

        long ruleFireCount = kSession.fireAllRules();
        System.out.println("Fired " + ruleFireCount + " rules.");

        if (handle != null) kSession.delete(handle);

        var filter = new ObjectFilter()
        {
            @Override
            public boolean accept( Object object )
            {
                return object.getClass().getName().equals(Solution.class.getName());
            }
        };

        var results = (Collection<Solution>) kSession.getObjects(filter);
        var solution = results.stream().findFirst().orElse(null);

        if (solution != null) {
            request.complete(solution);
            return ok(request);
        }

        request.solution.price++;
        return ok(diagnostic.currentMeasurement);
    }

}
