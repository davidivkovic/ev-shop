package sbnz.api;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import sbnz.users.User;
import sbnz.vehicles.Diagnostic;
import sbnz.vehicles.Problem;
import sbnz.vehicles.Solution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/diagnostics")
@SuppressWarnings("unchecked")
@Produces(MediaType.APPLICATION_JSON)
public class Diagnostics extends Resource {

    public static Map<String, KieSession> sessions = new HashMap<>();

    @DELETE
    @Path("/")
    @Authenticated
    public Response reset() {
        var user = userId();
        if (sessions.containsKey(user)) {
            sessions.get(user).dispose();
            sessions.remove(user);
        }
        return ok();
    }

    @POST
    @Path("/")
    @Authenticated
    public Response diagnose(@QueryParam("measurement") String measurement) {

        var user = userId();
        boolean initialSession = !sessions.containsKey(user);

        if (initialSession) {

            KieServices ks = KieServices.Factory.get();
            KieContainer kc = ks.newKieClasspathContainer();
            KieSession kSession = kc.newKieSession();

            kSession.setGlobal("diagnostic", new Diagnostic());

            sessions.put(user, kSession);

            var problem = new Problem(Problem.Types.CHARGING);
            kSession.insert(problem);
        }

        var session = sessions.get(user);
        var diagnostic = (Diagnostic) session.getGlobal("diagnostic");
        diagnostic.currentMeasurement = null;

        FactHandle handle = null;

        if (!initialSession && measurement != null) {
            var value = (measurement.equals("yes") || measurement.equals("no"))
                    ? measurement
                    : Double.parseDouble(measurement);
            handle = session.insert(value);
        }

        long ruleFireCount = session.fireAllRules();
        System.out.println("Fired " + ruleFireCount + " rules.");

        if (handle != null) session.delete(handle);

        var filter = new ObjectFilter()
        {
            @Override
            public boolean accept( Object object )
            {
                return object.getClass().getName().equals(Solution.class.getName());
            }
        };

        var results = (Collection<Solution>) session.getObjects(filter);
        if (results.size() > 0) return ok(results);

        return ok(diagnostic.currentMeasurement);
    }
}
