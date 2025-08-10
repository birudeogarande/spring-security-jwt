package com.saatvik.app.dto;

import java.util.List;

public record AuthenticationRequest(String username, String password, List<String> roles) {

}