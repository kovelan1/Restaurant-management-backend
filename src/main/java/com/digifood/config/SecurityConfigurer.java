package com.digifood.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private JwtFilter jwtFilter;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable().authorizeRequests().antMatchers("/","/actuator/health","/authenticate","/signup/{role}","/password/code/{username}","/password/reset")
        .permitAll().anyRequest().authenticated()
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth
		.jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled from users where username=?")
        .authoritiesByUsernameQuery("select username, role from users where username=?")
        .passwordEncoder(passwordEncoder());
		
		 
		
	}
}
