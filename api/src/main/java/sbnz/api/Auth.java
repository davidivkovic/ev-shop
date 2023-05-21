package sbnz.api;

import io.quarkus.security.Authenticated;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import sbnz.api.dto.auth.*;
import sbnz.users.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class Auth extends Resource {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Valid @NotNull AuthenticationRequest request) {
        var user = User.findByEmail(request.email);

        var success = User.authenticate(user, request.password);
        if (!success) return badRequest("The credentials you entered are not valid.");

        var token = user.generateToken();
        if (token == null) return badRequest("We could not log you in at this time. Please try again later.");

        var response = new AuthenticationResponse(user, token);
        return ok(response);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(@Valid @NotNull RegistrationRequest request) {
        var user = User.findByEmail(request.email);
        if (user != null) return badRequest("A user with this email already exists.");

        user = User.register(request.firstName, request.lastName, request.role, request.email, request.password);

        return ok();
    }

    @GET
    @Authenticated
    @Path("/testing")
    public Response testing() {
       return ok(userId());
    }

}