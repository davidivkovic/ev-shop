package sbnz.api;

import sbnz.users.User;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.Set;

public class Resource {

    @Context SecurityContext securityContext;

    public Response ok() {
        return Response.ok().build();
    }

    public Response ok(Object entity) { return Response.ok(entity).build(); }

    public Response ok(Object entity, String mediaType) {
        return Response.ok(entity, mediaType).build();
    }

    public Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public Response badRequest(Object entity) {
        return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
    }

    public Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Response unauthorized() { return Response.status(Response.Status.UNAUTHORIZED).build(); }

    public Response forbidden() { return Response.status(Response.Status.FORBIDDEN).build(); }

    public String userId() {
        return securityContext.getUserPrincipal().getName();
    }

}
