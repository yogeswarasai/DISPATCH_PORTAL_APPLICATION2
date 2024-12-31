package com.iocl.Dispatch_Portal_Application.Security;

import java.net.http.HttpClient;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
  

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean() ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
               .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/dispatch-app/**","/",
                	    "/swagger-ui.html",
                	    "/v3/api-docs/**","/api/v1/employee/**","/roles/**","/users/**","/api/v1/dispatch/**","/parcels-in/**","/parcels-out/**","/api/menus/**","/sequences/**","api/courier-contracts",  "/favicon.ico","/index.html", "/static/**",  "/assets/**","/styles-YQW3JFZ4.css","/polyfills-6EAL64PA.js","/main-JXTZLB2E.js","/chunk-NSLGAEX5.js","/chunk-6LSQCF26.js").permitAll()
//               .antMatchers(
//                       "/dispatch-app/swagger-ui/**",
//                       "/dispatch-app/swagger-ui.html",
//                       "/dispatch-app/v3/api-docs/**",
//                       "/dispatch-app/api/v1/employee/**",
//                       "/dispatch-app/roles/**",
//                       "/dispatch-app/users/**",
//                       "/dispatch-app/api/v1/dispatch/**",
//                       "/dispatch-app/parcels-in/**",
//                       "/dispatch-app/parcels-out/**",
//                       "/dispatch-app/api/menus/**",
//                       "/dispatch-app/sequences/**",
//                       "/dispatch-app/api/courier-contracts/**"
//                   ).permitAll()
               //       .antMatchers("/api/v1/dispatch/**").hasRole("DISPATCH")
                .anyRequest()
                .authenticated();

        http.headers().frameOptions().sameOrigin();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}