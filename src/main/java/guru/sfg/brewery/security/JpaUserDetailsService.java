package guru.sfg.brewery.security;

import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //        return new User(user.getUsername(), user.getPassword(), user.getEnabled(), user.getAccountNonExpired(),
//                user.getCredentialsNonExpired(), user.getAccountNonLocked(), convertToSpringAuthorities(user.getAuthorities()));

        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User :" + username + " not found"));
    }
//
//    private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
//        if (authorities == null)
//            return new HashSet<>();
//
//        return authorities.stream()
//                .map(Authority::getPermission)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toSet());
//    }
}
