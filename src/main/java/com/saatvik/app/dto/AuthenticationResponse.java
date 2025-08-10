package com.saatvik.app.dto;

import java.util.List;

public record AuthenticationResponse(String token, List<String> roles, String username) {
}
