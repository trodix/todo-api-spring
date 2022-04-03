package com.trodix.todoapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.trodix.todoapi.model.request.LoginRequest;
import com.trodix.todoapi.model.response.JwtResponse;
import com.trodix.todoapi.service.AuthService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"dev", "test"})
@Sql(scripts = "/data/data-h2.sql")
@Transactional
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Test
    public void itShouldReturnLoggedInUser() throws Exception {

        final JSONObject payload = new JSONObject();
        payload.put("username", "userTest");
        payload.put("password", "userTest");

        this.mockMvc
                .perform(post("/api/public/auth/signin").contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.username").value(payload.get("username")));
    }

    @Test
    public void itShouldReturnCreatedUser() throws Exception {

        final JSONArray roleList = new JSONArray();
        roleList.put("admin");

        final JSONObject payload = new JSONObject();
        payload.put("username", "userTest2");
        payload.put("password", "userTest2");
        payload.put("email", "userTest2@example.com");
        payload.put("roles", roleList);

        this.mockMvc.perform(
                post("/api/public/auth/signup").contentType(MediaType.APPLICATION_JSON).content(payload.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void itShouldReturnRefreshedToken() throws Exception {

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("userTest");
        loginRequest.setPassword("userTest");

        final JwtResponse jwtResponse = this.authService.authenticateUser(loginRequest);
        assertEquals("userTest", jwtResponse.getUsername());

        final JSONObject payload = new JSONObject();
        payload.put("username", jwtResponse.getUsername());
        payload.put("refreshToken", jwtResponse.getRefreshToken());

        final MvcResult result = this.mockMvc.perform(post("/api/public/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON).content(payload.toString())).andExpect(status().isOk())
                .andReturn();

        final JSONObject obj = new JSONObject(result.getResponse().getContentAsString());
        assertEquals(obj.get("refreshToken"), jwtResponse.getRefreshToken());
        // assertNotEquals(obj.get("accessToken"), jwtResponse.getAccessToken());
    }

    @Test
    public void itShouldInvalidateRefreshToken() throws Exception {

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("userTest");
        loginRequest.setPassword("userTest");

        final JwtResponse jwtResponse = this.authService.authenticateUser(loginRequest);
        assertEquals("userTest", jwtResponse.getUsername());

        final JSONObject payload = new JSONObject();
        payload.put("username", jwtResponse.getUsername());
        payload.put("refreshToken", jwtResponse.getRefreshToken());

        this.mockMvc.perform(
                post("/api/public/auth/logout").contentType(MediaType.APPLICATION_JSON).content(payload.toString()))
                .andExpect(status().isNoContent()).andReturn();

        this.mockMvc.perform(post("/api/public/auth/refresh-token").contentType(MediaType.APPLICATION_JSON)
                .content(payload.toString())).andExpect(status().isUnauthorized());
    }

}
