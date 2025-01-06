package Outpatient.example.Intership_Backend.Configuartion;


import Outpatient.example.Intership_Backend.Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests

                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/doctor/**","/api/patient/**","/appointments/**","/notifications/**","/feedback/**","/**").permitAll()
                                .requestMatchers("/prescriptions/**","/prescriptions","/api/admin/**","/api/admin/delete/**").permitAll()
                                .anyRequest().authenticated());

        return http.build();

    }


}