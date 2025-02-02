package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseIT {

    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

    }

    public static Stream<Arguments> getStreamOfAdminCustomer() {
        return Stream.of(Arguments.of("spring", "guru"), Arguments.of("scott", "tiger"));
    }

    public static Stream<Arguments> getStreamOfAllUsers() {
        return Stream.of(Arguments.of("spring", "guru"), Arguments.of("user", "password"), Arguments.of("scott", "tiger"));
    }

    public static Stream<Arguments> getStreamOfNonAdmin() {
        return Stream.of(Arguments.of("user", "password"), Arguments.of("scott", "tiger"));
    }
}
