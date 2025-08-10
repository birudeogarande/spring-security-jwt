package com.saatvik.app.api;

import com.google.gson.Gson;
import com.saatvik.app.dto.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    private AuthenticationResponse authenticationResponse;

    @Autowired
    private  MockMvc mvc;

    /**
     * This test class is used to test the UserController endpoints.
     * It uses MockMvc to perform requests and verify responses.
     * The tests cover both authenticated and unauthenticated requests.
     */

    @BeforeEach
    public void setUp() throws Exception {

        mvc.perform(post("/auth/register")
                .contentType("application/json")
                .content("""
                        {
                            "username": "admin",
                            "password": "admin",
                            "roles": ["ROLE_ADMIN"]
                        }
                        """))
                .andExpect(status().isOk());
     }

     @BeforeEach
     public void loginTest() throws Exception {
         mvc.perform(post("/auth/login")
                         .contentType("application/json")
                         .content("""
                        {
                            "username": "admin",
                            "password": "admin"
                        }
                        """))
                 .andExpect(status().isOk())
                 .andDo(result -> {
                     String response = result.getResponse().getContentAsString();
                     Gson gson = new Gson();
                     this.authenticationResponse = gson.fromJson(response, AuthenticationResponse.class);

                     System.out.println("Login Response: " + response);
                 });
     }

    @Test
    void sayHello() throws Exception {
        mvc.perform(
                get("/api/user")
                        .contentType("application/json")
        )
                .andExpect(status().is4xxClientError());
                // Expecting a 4xx error because no authentication is provided
    }

    @Test
    void sayHelloOK() throws Exception {
        mvc.perform(
                        get("/api/user")
                                .header("Authorization", "Bearer " + authenticationResponse.token()) // Use the token from the login response
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello User!!"));
    }



    @Test
    void sayHelloToAdmin() throws Exception {

        mvc.perform(
                get("/api/admin")
                        .header("Authorization", "Bearer " + authenticationResponse.token()) // Use the token from the login response
                        .contentType("application/json")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Admin!!"));
    }

    @Test
    void sayHelloAdminFail() throws Exception {
        mvc.perform(
                get("/api/admin")
                        .header("Authorization", "Bearer akjcnkajnsfknakjnkjn") // Use the token from the login response
                        .contentType("application/json")
        )
                .andExpect(status().is4xxClientError()); // Expecting a 403 Forbidden error because user does not have admin role
    }
}