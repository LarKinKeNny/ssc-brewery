package guru.sfg.brewery.security.listeners;


import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.warn(event.getException().getMessage());
        if(event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            var loginFailureBuilder = LoginFailure.builder();
            var source = (UsernamePasswordAuthenticationToken) event.getSource();

            var username = (String) source.getPrincipal();
            loginFailureBuilder.username(username);

            var userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                var user = userOptional.get();
                loginFailureBuilder.user(user);

                var failedCount = loginFailureRepository.countLoginFailuresByUserAndCreatedDateIsAfter(user,
                        Timestamp.valueOf(LocalDateTime.now().minusMinutes(30)));
                log.warn("Failed count: {}", failedCount);
                if (failedCount >= 2) {
                    lockUserAccount(user);
                }
            }

            var details = source.getDetails();
            if(details instanceof WebAuthenticationDetails) {
                var remoteAddress = ((WebAuthenticationDetails) details).getRemoteAddress();
                log.warn("There was an attempt to login with bad credentials username used: {} from {} ",
                        source.getPrincipal(), remoteAddress);
                loginFailureBuilder.sourceIp(remoteAddress);
            }
            loginFailureRepository.save(loginFailureBuilder.build());
        }
    }

    private void lockUserAccount(User user) {
        log.debug("Locking userAccount {}", user.getUsername());
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }
}
