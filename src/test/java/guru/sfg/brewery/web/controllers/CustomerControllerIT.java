package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIT extends BaseIT {

    @DisplayName("List Customers")
    @Nested
    class ListCustomersTests {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIT#getStreamOfAdminCustomer")
        void testListCustomersAUTH(String user, String password) throws Exception {
            mockMvc.perform(get("/customers")
                            .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }

        @Test
        void testListCustomersNOAUTH() throws Exception {
            mockMvc.perform(get("/customers")
                            .with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void testListCustomersUser() throws Exception {
            mockMvc.perform(get("/customers")
                            .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Add Customers")
    @Nested
    class AddCustomerTests {

        @Rollback
        @Test
        void processCreationFormAdmin() throws Exception {
            mockMvc.perform(post("/customers/new").with(csrf())
                    .param("customerName", "Foo Customer")
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is3xxRedirection());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIT#getStreamOfNonAdmin")
        void processCreationFormNonAdmin(String user, String password) throws Exception {
            mockMvc.perform(post("/customers/new")
                            .param("customerName", "Foo Customer")
                            .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void processCreationFormNoAuth() throws Exception {
            mockMvc.perform(post("/customers/new").with(csrf())
                            .param("customerName", "Foo Customer")
                            .with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }
}
