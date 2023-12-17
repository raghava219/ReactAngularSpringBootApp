package net.javaguides.springboot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAthFilter jwtAuthFilter;

    private final static List<UserDetails> APPLICATION_USERS = Arrays.asList(
        new User(
                "sample_user_name1@gmail.com",
                "password1",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ),
        new User(
               "user_mail2@gmail.com",
               "password2",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        ),
        new User(
                "user_mail3@gmail.com",
                "password3",
                 Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        )
    );
    // Below Code is from SpringBootWebSecurityConfiguration
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // After above change, we can comment below two lines
        // http.formLogin();
        // http.httpBasic();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                // We can connect to DB and fetch the details
                return APPLICATION_USERS
                        .stream()
                        .filter(u -> u.getUsername().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
            }
        };
    }

}