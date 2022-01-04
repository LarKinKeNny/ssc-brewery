package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginSuccess;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginSuccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationSuccessListener {

    private final LoginSuccessRepository loginSuccessRepository;

    @EventListener
    public void listen(AuthenticationSuccessEvent event) {
        if (event.getAuthentication().getPrincipal() instanceof User) {
            var loginSuccessBuilder = LoginSuccess.builder();
            loginSuccessBuilder.user(((User) event.getAuthentication().getPrincipal()));

            if (event.getAuthentication().getDetails() instanceof WebAuthenticationDetails) {
                var remoteAddress = ((WebAuthenticationDetails) event.getAuthentication().getDetails()).getRemoteAddress();
                log.debug("User: {} logged in from {}", ((User) event.getAuthentication().getPrincipal()).getUsername(),
                        remoteAddress);

                loginSuccessBuilder.sourceIp(remoteAddress);
            }
            loginSuccessRepository.save(loginSuccessBuilder.build());
        }
    }
}
