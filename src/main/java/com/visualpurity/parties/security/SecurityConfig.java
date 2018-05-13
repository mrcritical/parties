package com.visualpurity.parties.security;

import com.visualpurity.parties.security.token.ApiAuthenticationProvider;
import com.visualpurity.parties.security.token.TokenLoginSuccessHandler;
import com.visualpurity.parties.security.token.TokenAuthenticationFilter;
import com.visualpurity.parties.security.token.TokenAuthenticationUserDetailsService;
import com.visualpurity.parties.security.token.TokenLogoutSuccessHandler;
import com.visualpurity.parties.security.user.UserSecurityProperties;
import com.visualpurity.parties.security.user.UsernamePasswordDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableConfigurationProperties(UserSecurityProperties.class)
public class SecurityConfig {

    @NonNull
    private final UsernamePasswordDetailsService usernamePasswordDetailsService;

    @NonNull
    private final UserSecurityProperties userSecurityProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(userSecurityProperties.getStrength());
    }

    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public class AdminAuthConfig extends WebSecurityConfigurerAdapter {

        @NonNull
        private final TokenLoginSuccessHandler tokenLoginSuccessHandler;

        @NonNull
        private final TokenLogoutSuccessHandler tokenLogoutSuccessHandler;

        @NonNull
        private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/admin/**")
                    .formLogin()
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login")
                    .permitAll()
                    .successHandler(tokenLoginSuccessHandler)
                    .and()
                    .logout()
                    .logoutUrl("/admin/logout")
                    .logoutSuccessHandler(tokenLogoutSuccessHandler)
                    .permitAll()
                    .and()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .addFilterBefore(authFilter(), RequestHeaderAuthenticationFilter.class)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and().csrf().disable();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(usernamePasswordDetailsService)
                    .passwordEncoder(passwordEncoder());
            auth.authenticationProvider(preAuthProvider());
        }

        @Bean
        public TokenAuthenticationFilter authFilter() {
            return new TokenAuthenticationFilter();
        }

        @Bean
        public AuthenticationProvider preAuthProvider() {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);
            return provider;
        }

    }

    @Configuration
    @Order(2)
    @RequiredArgsConstructor
    public class TokenIssuerAuthConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/token")
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(usernamePasswordDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

    }

    @Configuration
    @Order(3)
    @RequiredArgsConstructor
    public class TokenAuthConfig extends WebSecurityConfigurerAdapter {

        @NonNull
        private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/admin/**")
                    .authorizeRequests()
                    .mvcMatchers(HttpMethod.POST, "/api/admin/accounts/signUp")
                    .anonymous()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .addFilterBefore(authFilter(), RequestHeaderAuthenticationFilter.class)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf()
                    .disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(preAuthProvider())
                    .authenticationProvider(apiAuthenticationProvider());
        }

        @Bean
        public TokenAuthenticationFilter authFilter() {
            return new TokenAuthenticationFilter();
        }

        @Bean
        public AuthenticationProvider preAuthProvider() {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);
            return provider;
        }

        @Bean
        public ApiAuthenticationProvider apiAuthenticationProvider() {
            return new ApiAuthenticationProvider();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }

}
