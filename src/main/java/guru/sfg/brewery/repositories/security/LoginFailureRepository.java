package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;

public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer> {
    Long countLoginFailuresByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
}
