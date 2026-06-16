package com.plantsync.platform.iam.infrastructure.authorization.sfs.configuration;

import com.plantsync.platform.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.plantsync.platform.iam.infrastructure.hashing.bcrypt.BcryptHashingService;
import com.plantsync.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Web Security Configuration.
 *
 * <p>This class is responsible for configuring the web security.
 * It enables the method security and configures the security filter chain.
 * It includes the authentication manager, the authentication provider,
 * the password encoder and the authentication entry point.</p>
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

  private final UserDetailsService userDetailsService;

  private final BearerTokenService tokenService;

  private final BcryptHashingService hashingService;

  private final AuthenticationEntryPoint unauthorizedRequestHandler;

  private final List<String> allowedOrigins;

  /**
   * This method creates the Bearer Authorization Request Filter.
   *
   * @return The Bearer Authorization Request Filter
   * @see BearerAuthorizationRequestFilter
   */
  @Bean
  public BearerAuthorizationRequestFilter authorizationRequestFilter() {
    return new BearerAuthorizationRequestFilter(tokenService, userDetailsService);
  }

  /**
   * This method creates the authentication manager.
   *
   * @param authenticationConfiguration The {@link AuthenticationConfiguration} object with the
   *                                  authentication configuration.
   * @return The {@link AuthenticationManager} instance from the authentication configuration.
   *
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * This method creates the authentication provider.
   *
   * @return The authentication provider with the user details service and password encoder.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    var authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
    authenticationProvider.setPasswordEncoder(hashingService);
    return authenticationProvider;
  }

  /**
   * This method creates the password encoder.
   *
   * @return The {@link PasswordEncoder} instance with the hashing service.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return hashingService;
  }

  /**
   * This method creates the security filter chain.
   * It also configures the http security.
   *
   * @param http The {@link HttpSecurity} object to configure with the security filter chain.
   * @return The {@link SecurityFilterChain} instance with the application http security
   *         configuration.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(configurer -> configurer.configurationSource(request -> {
      var cors = new CorsConfiguration();
      cors.setAllowedOrigins(allowedOrigins);
      cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
      cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
      return cors;
    }));
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(unauthorizedRequestHandler))
        .sessionManagement(customizer -> customizer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers(
                "/api/v1/authentication/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/api/payments/create-session",
                "/webjars/**").permitAll()
            .anyRequest().authenticated());
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();

  }

  /**
   * This is the constructor of the class.
   *
   * @param userDetailsService       The user details service.
   * @param tokenService             The token service.
   * @param hashingService           The hashing service.
   * @param authenticationEntryPoint The authentication entry point.
   * @param allowedOrigins           Comma-separated list of allowed CORS origins.
   */
  public WebSecurityConfiguration(
      @Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
      BearerTokenService tokenService, BcryptHashingService hashingService,
      AuthenticationEntryPoint authenticationEntryPoint,
      @Value("${cors.allowed-origins}") String allowedOrigins) {
    this.userDetailsService = userDetailsService;
    this.tokenService = tokenService;
    this.hashingService = hashingService;
    this.unauthorizedRequestHandler = authenticationEntryPoint;
    this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
        .map(String::trim)
        .filter(origin -> !origin.isEmpty())
        .toList();
  }
}
