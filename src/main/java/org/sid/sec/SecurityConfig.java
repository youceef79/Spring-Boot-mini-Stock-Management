package org.sid.sec;

import java.awt.Container;
import java.awt.event.ContainerAdapter;

import javax.sql.DataSource;

import org.aspectj.weaver.ast.And;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	private UserPrincipalDetailsService userPrincipalDetailsService;

	
    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }
	
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	/*
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}*/
	
	@Bean
	public static PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	  
	@Override
	    protected void configure(AuthenticationManagerBuilder auth) {
	        auth.authenticationProvider(authenticationProvider());
	    }
	 
	 /*
	
	  @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//pour definir la maniere dont on va chercher les utilisateurs
		auth.inMemoryAuthentication()
			.withUser("admin").password("1234").roles("ADMIN","USER");
		auth.inMemoryAuthentication()
			.withUser("user").password("1234").roles("USER"); 
		//auth.authenticationProvider(authProvider());
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("Select username as principal, password as credentials from users where username=? ")
			.authoritiesByUsernameQuery("Select username as principal, role from user_role where username=? ")
			.rolePrefix("ROLE_")
			.passwordEncoder(new BCryptPasswordEncoder());
		}
	*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// pour definir les strategies de securités,, les regles
		//on demande a Spring qu'on a besoin de passer par une authentification basée par un formulaire
//		http.formLogin().defaultSuccessUrl("/prods");
		//securiser les ressources de l'appli
//		http.authorizeRequests().antMatchers("/operations","/consulterCompte").hasRole("USER");  
//		http.authorizeRequests().antMatchers("/saveOperation").hasRole("ADMIN");
		
		http
        .authorizeRequests()
        .antMatchers("/").authenticated()
        .and()
        .formLogin().loginProcessingUrl("/signin").defaultSuccessUrl("/prods").loginPage("/login")
        .and()
        .authorizeRequests()
        .antMatchers("/register").permitAll()
        .antMatchers("/signup").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/prods" , "/frnss" , "/clients").authenticated()
        .antMatchers("/imports" , "/exports").hasRole("ADMIN")
        .and()
        .exceptionHandling().accessDeniedPage("/404")
        .and()
        .authorizeRequests()
        .antMatchers("/*").authenticated()
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
        .and()
        .rememberMe().tokenValiditySeconds(2592000).key("mySecret!").rememberMeParameter("checkRememberMe");
		
//		.antMatchers("/management/**").hasAnyRole("ADMIN", "MANAGER")
//        .antMatchers("/api/public/test1").hasAuthority("ACCESS_TEST1")
//        .antMatchers("/api/public/test2").hasAuthority("ACCESS_TEST2")
//        .antMatchers("/api/public/users").hasRole("ADMIN")

		
		    //http.exceptionHandling().accessDeniedPage("/403");
	}
	
	 @Bean
	    DaoAuthenticationProvider authenticationProvider(){
	        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	        daoAuthenticationProvider.setPasswordEncoder(encoder());
	        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

	        return daoAuthenticationProvider;
	    }
	 
}
