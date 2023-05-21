package sbnz.api.dto.auth;

import sbnz.users.User;

public record AuthenticationResponse(User user, String token) { }