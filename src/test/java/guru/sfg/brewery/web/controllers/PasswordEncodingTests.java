package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordEncodingTests {
    static final String PASSWORD = "password";

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        String salted = PASSWORD + LocalDateTime.now();
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }

    @Test
    void testNoOp() {
        var encoder = NoOpPasswordEncoder.getInstance();
        System.out.println(encoder.encode(PASSWORD));
    }

    @Test
    void testLdap() {
        var encoder = new LdapShaPasswordEncoder();
        System.out.println(encoder.encode("tiger"));

        var pass = encoder.encode(PASSWORD);
        System.out.println(pass);

        assert encoder.matches(PASSWORD, pass);
    }

    @Test
    void testSha256() {
        var encoder = new StandardPasswordEncoder();
        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode(PASSWORD));
    }

    @Test
    void testBCrypt() {
//        var encoder = new BCryptPasswordEncoder(10);
//        System.out.println(encoder.encode("tiger"));
//        System.out.println(encoder.encode(PASSWORD));
        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID());
    }
}
