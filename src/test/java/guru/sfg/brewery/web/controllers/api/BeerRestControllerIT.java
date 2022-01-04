package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerRestControllerIT extends BaseIT {

//    @Test
//    void deleteBeerHttpBasic() throws Exception {
//        mockMvc.perform(delete("/api/v1/beer/7e4a991b-e94a-49e7-896f-1184667cb546")
//                        .with(httpBasic("spring", "guru")))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    void deleteBeerUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/eb0cc5bc-3850-4d72-aa67-72df0b05b420")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerCustomerRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/eb0cc5bc-3850-4d72-aa67-72df0b05b420")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/eb0cc5bc-3850-4d72-aa67-72df0b05b420" ))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void listBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }

//    @Test
//    void getBeerById() throws Exception {
//        mockMvc.perform(get("/api/v1/beer/0c6c549c-1b6e-488e-a54d-582136c8a388"))
//                .andExpect(status().isOk());
//    }

    @Test
    void findBeerByUPC() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }
}