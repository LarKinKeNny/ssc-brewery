package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.security.google.Google2faFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(/*securedEnabled = true,*/ prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
//        var filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }
//
//    public RestParameterAuthFilter restParameterAuthFilter(AuthenticationManager authenticationManager) {
//        var filter = new RestParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final Google2faFilter google2faFilter;

    // needed for use with Spring Data JPA SPeL
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/h2-console/**", "/api/**");
        http.addFilterBefore(google2faFilter, SessionManagementFilter.class);

//        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(restParameterAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests(authorize -> authorize
                                .antMatchers("/h2-console/**").permitAll()
                                .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
//                        .antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("CUSTOMER", "ADMIN", "USER")
//                        .antMatchers("/beers/find", "/beers*").hasAnyRole("CUSTOMER", "ADMIN", "USER")
//                        .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")

//                        .mvcMatchers(HttpMethod.GET, "/brewery/breweries/**", "/brewery/api/v1/breweries")
//                            .hasAnyRole("CUSTOMER", "ADMIN"))

//                        .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("CUSTOMER", "ADMIN", "USER"))
                ).authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(x -> x.loginProcessingUrl("/login")
                        .loginPage("/").permitAll()
                        .successForwardUrl("/")
                        .defaultSuccessUrl("/")
                        .failureUrl("/?error"))
                .logout(x -> x.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/?logout").permitAll())
                .httpBasic()
                .and().rememberMe()
                        .tokenRepository(persistentTokenRepository)
//                .key("sfg-key")
                .userDetailsService(userDetailsService);

        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                .password("{bcrypt}$2a$12$/5Clpf5852JJCKLY4RT75.ZO1ww5sfsQi.tJYaQ7zqdgWHa40UW.y")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}8ae31363f256ff5a129b90e2cf15287eb2a01a1f8832fa7d1a86ac9d14b35a705e2fe0440c5e2013")
//                .roles("USER")
//                .and()
//                .withUser("scott")
//                .password("{bcrypt10}$2a$15$5tfGttuv/NnNNE77wUmoF.Q9.f4h4yAVe0MxtBPT3Ukiihz.75gJi")
//                .roles("CUSTOMER");
//    }
//
//        @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        var admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        var user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
