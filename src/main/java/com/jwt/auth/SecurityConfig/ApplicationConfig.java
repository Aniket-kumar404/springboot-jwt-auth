package com.jwt.auth.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.auth.service.UserdetailServiceImplementation;
//
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;


@Configuration
@EnableWebSecurity
public class ApplicationConfig {

    @Autowired
    private JwtTokenValidator authFilter;
    
	@Autowired
    private UserdetailServiceImplementation userDetailService;
	
    @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .cors(cors -> cors.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/auth/**").permitAll().
                                requestMatchers("/api/**").hasAuthority("admin").
                                requestMatchers("/user/**").hasAuthority("user")
                                .requestMatchers("/swagger-ui.html", "/v3/api-docs", "/swagger-ui/**").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement(session -> session

                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .maximumSessions(2)
                        .expiredUrl("/login?expired=true")
        );
        return http.build();
    }


    @Bean
     AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
     AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder(); 
   }

}
