package sbnz.api;

import io.quarkus.security.Authenticated;
import org.bson.types.ObjectId;
import org.drools.io.ByteArrayResource;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.utils.KieHelper;
import sbnz.api.dto.repair_requests.RepairRequestDTO;
import sbnz.users.User;
import sbnz.vehicles.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/repair-requests")
@Produces(MediaType.APPLICATION_JSON)
public class RepairRequests extends Resource {

    public record Session(RepairRequest request, KieSession kSession, KieSession drtKSession) { }

    public static Map<String, Session> sessions = new HashMap<>();

    private KieSession createKieSessionFromDRL(List<String> rules){

        var kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        rules.forEach(rule -> kbuilder.add(new ByteArrayResource(rule.getBytes()), ResourceType.DRL));
        var kbase = kbuilder.newKieBase();
        return kbase.newKieSession();

//        Results results = kieHelper.verify();
//
//        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
//            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
//            for (Message message : messages) {
//                System.out.println("Error: "+message.getText());
//            }
//
//            throw new IllegalStateException("Compilation errors were found. Check the logs.");
//        }

//        return kieHelper.build().newKieSession();
    }

    @GET
    @Path("/problems")
    public Response getProblems() {
        return ok(Problem.types);
    }

    @POST
    @Path("/{id}/update-status")
    @Authenticated
    public Response update(@PathParam("id") String id, RepairRequestDTO dto) {
        RepairRequest request = RepairRequest.findById(new ObjectId(id));
        if (request == null) return badRequest("Request not found.");

        if (dto.accepted) request.accept(dto.scheduledAt);
        else request.reject();

        request.update();

        return ok();
    }

    @GET
    @Authenticated
    @Path("/")
    public Response getAll() {
        User user = User.findById(userId());
        if (user == null) return badRequest("User not found.");

        return user.isRole(User.Roles.CUSTOMER)
            ? ok(RepairRequest.list("userId = ?1", userId()))
            : ok(RepairRequest.list("shop._id = ?1", user.shop.id));
    }

    @GET
    @Authenticated
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        var request = RepairRequest.findById(new ObjectId(id));
        if (request == null) return badRequest("Request not found.");

        return ok(request);
    }

    @POST
    @Path("/{id}/reset-diagnostics")
    @Authenticated
    public Response reset(@PathParam("id") String id) {
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
            kSession.setGlobal("vehicle", request.vehicle);

            RepairShop shop = RepairShop.findById(request.shop.id);
            sessions.put(user, new Session(
                request, kSession, createKieSessionFromDRL(shop.partQuantityAlarmRules)
            ));

            var problem = new Problem(request.problem.type());
            kSession.insert(problem);
        }

        var session = sessions.get(user);

        var kSession = session.kSession;
        var drtKSession = session.drtKSession;
        var request = session.request;

        var diagnostic = (Diagnostic) session.kSession.getGlobal("diagnostic");
        diagnostic.currentMeasurement = null;

        FactHandle handle = null;

        if (measurement != null && !measurement.equals("null")) {
            var value = (measurement.equals("yes") || measurement.equals("no"))
                    ? measurement
                    : Double.parseDouble(measurement);
            handle = kSession.insert(value);
        }

        long ruleFireCount = kSession.fireAllRules();
        System.out.println("Fired " + ruleFireCount + " rules.");

        if (handle != null) kSession.delete(handle);

        var results = (Collection<Solution>) kSession.getObjects(
            object -> object.getClass().getName().equals(Solution.class.getName())
        );
        var solution = results.stream().findFirst().orElse(null);

        if (solution != null) {
            request.complete(solution);

            var requestHandle = kSession.insert(request);
            kSession.fireAllRules();
            kSession.delete(requestHandle);

            var alarms = (Collection<Part.QuantityAlarm>) kSession.getObjects(
                object -> object.getClass().getName().equals(Part.QuantityAlarm.class.getName())
            );
            request.alarm = alarms.stream().findFirst().orElse(null);

            kSession.dispose();
            drtKSession.dispose();
            sessions.remove(user);

            return ok(request);
        }

        request.solution.price++;
        return ok(diagnostic.currentMeasurement);
    }

}
