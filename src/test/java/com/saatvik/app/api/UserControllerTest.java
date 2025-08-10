//package com.saatvik.app.api;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Test
//    void sayHello() throws Exception {
//        mvc.perform(
//                get("/api/user")
//                        .contentType("application/json")
//        )
//                .andExpect(status().is4xxClientError());
//                // Expecting a 4xx error because no authentication is provided
//    }
//
//    @Test
//    void sayHelloOK() throws Exception {
//        mvc.perform(
//                        get("/api/user")
//                                .with(httpBasic("user", "user")) // Add Basic Auth credentials
//                                .contentType("application/json")
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello User!!"));
//    }
//
//
//
//    @Test
//    void sayHelloToAdmin() throws Exception {
//
//        mvc.perform(
//                get("/api/admin")
//                        .with(httpBasic("admin", "admin")) // Add Basic Auth credentials for admin
//                        .contentType("application/json")
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello Admin!!"));
//    }
//
//    @Test
//    void sayHelloAdminFail() throws Exception {
//        mvc.perform(
//                get("/api/admin")
//                        .with(httpBasic("user", "user")) // Add Basic Auth credentials for user
//                        .contentType("application/json")
//        )
//                .andExpect(status().isForbidden()); // Expecting a 403 Forbidden error because user does not have admin role
//    }
//}