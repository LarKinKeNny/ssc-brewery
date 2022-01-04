package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BreweryControllerIT extends BaseIT {

    @Test
    void listBreweriesUser() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesCustomer() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesAdmin() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesAnon() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweriesApiUser() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesApiCustomer() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesApiAdmin() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesApiAnon() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }
}