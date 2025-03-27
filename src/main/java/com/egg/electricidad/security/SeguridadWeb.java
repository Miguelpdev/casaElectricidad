package com.egg.electricidad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.egg.electricidad.service.UsuarioService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SeguridadWeb {

        @Autowired
        private UsuarioService usuarioService;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http
        // .authorizeHttpRequests((authorize) -> authorize
        // .requestMatchers("/css/", "/js/", "/img/", "/**").permitAll())
        // .csrf(csrf -> csrf.disable());
        // return http.build();
        // }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/admin/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                                .requestMatchers("/", "/inicio", "/login", "/usuario/registrar",
                                                                "/usuario/registro")
                                                .permitAll() // Permitir
                                                .anyRequest().authenticated() // Requiere autenticacion
                                )
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/logincheck")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/inicio", true)
                                                .permitAll())
                                .logout((logout) -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login")
                                                .permitAll())
                                .csrf(csrf -> csrf.disable());
                return http.build();
        }

}
