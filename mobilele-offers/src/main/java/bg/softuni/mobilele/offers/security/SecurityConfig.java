package bg.softuni.mobilele.offers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity,
      JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize.requestMatchers(HttpMethod.GET, "/offers/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();

  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new AuthenticationProvider() {
      @Override
      public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {
        return null;
      }

      @Override
      public boolean supports(Class<?> authentication) {
        return false;
      }
    };
  }
}
