package com.portal.bid.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.portal.bid.filter.ApiKeyAuthFilter;
import com.portal.bid.filter.SecurityFilter;

import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder;

    @Autowired
    private UnAuthorizedUserAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private SecurityFilter secFilter;

    @Autowired
    private ApiKeyAuthFilter apiKeyAuthFilter;
    
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configure the AuthenticationManager bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configure SAML relying party registration using metadata.
     */
    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
        RelyingPartyRegistration registration = RelyingPartyRegistrations
                .fromMetadataLocation("classpath:GoogleIDPMetadata_STLSalesNxt.xml")
                .registrationId("google")
                .entityId("https://stlsalesnxt.sterliteapps.com:61445/") // Service Provider Entity ID
                .assertingPartyDetails(party -> {
                    party.entityId("https://accounts.google.com/o/saml2?idpid=C01qgd39r") // From metadata
                            .singleSignOnServiceLocation("https://accounts.google.com/o/saml2/idp?idpid=C01qgd39r");
                })
                .assertionConsumerServiceLocation("https://stlsalesnxt.sterliteapps.com:61445/saml/acs")
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }

    /**
     * SAML security filter chain.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain samlFilterChain(HttpSecurity http) throws Exception {
        // Debug: Entry point for SAML filter chain configuration
        log.debug("Configuring SAML Security Filter Chain");

        http.securityMatcher("/", "/saml2/**", "/login/saml2/**")
                .authorizeHttpRequests(authorize -> {
                    // Debug: Setting up authorization rules
                    log.debug("Setting authorization rules for SAML endpoints");
                    authorize
                            .requestMatchers("/", "/saml2/**", "/login/saml2/**").permitAll()
                            .anyRequest().authenticated();
                })
                .saml2Login(saml2 -> {
                    // Debug: Configuring SAML2 login
                    log.debug("Configuring SAML2 Login with relyingPartyRegistrationRepository");
                    saml2
                            .relyingPartyRegistrationRepository(relyingPartyRegistrationRepository()) // Ensure this is
                                                                                                      // set correctly
                            .defaultSuccessUrl("/api/user/sso/login", true)
                            .failureUrl("/api/user/sso-error?error=true");
                })
                .exceptionHandling(ex -> {
                    // Debug: Configuring exception handling
                    log.debug("Configuring exception handling");
                    ex.authenticationEntryPoint(authenticationEntryPoint) // Ensure this is correctly defined
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                log.error("Access Denied: {}", accessDeniedException.getMessage());
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                            });
                })
                .csrf(csrf -> {
                    // Debug: Disabling CSRF
                    log.warn("CSRF is disabled for SAML endpoints");
                    csrf.disable();
                });

        // Debug: SAML Security Filter Chain configured successfully
        log.debug("SAML Security Filter Chain configuration complete");

        return http.build();
    }

    /**
     * API key-based security filter chain.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiKeyFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/scrape/receive")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        return http.build();
    }

    /**
     * Public endpoints filter chain.
     */
    @Bean
    @Order(3)
    public SecurityFilterChain publicEndpointsFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/user/loginUser", "/api/user/renewToken", "/saml/acs")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

        return http.build();
    }

    /**
     * Default security filter chain for other endpoints.
     */
    @Bean
    @Order(4)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(secFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * Configure the authentication provider using DAO and BCrypt password encoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptEncoder);
        return authenticationProvider;
    }
}
