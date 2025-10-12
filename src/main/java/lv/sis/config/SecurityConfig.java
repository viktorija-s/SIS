package lv.sis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lv.sis.service.impl.MyUserDetailsManagerServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
//	@Bean
//	public UserDetailsManager createUsers() {
//		PasswordEncoder passEnc = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		
//		String encPassword = passEnc.encode("somepassword");
//		UserDetails user1 = User.builder().username("lisa").password(encPassword).authorities("ADMIN").build();
//		UserDetails user2 = User.builder().username("user").password(passEnc.encode("user")).authorities("USER").build();
//		
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(user1, user2);
//		return manager;
//	}
	
	@Bean
	public MyUserDetailsManagerServiceImpl loadMyUserDetailsManager() {
		return new MyUserDetailsManagerServiceImpl();
	}
	
	@Bean
	public DaoAuthenticationProvider loadDaoAuthProvider() {
		PasswordEncoder passEnc = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		MyUserDetailsManagerServiceImpl service = loadMyUserDetailsManager();
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passEnc);
		provider.setUserDetailsService(service);
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain configureUrlsSecurity(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth 
				.requestMatchers("/home").permitAll() // main lapai
				
				.requestMatchers("/email").hasAnyAuthority("ADMIN", "USER")
				
				.requestMatchers("/kursaDalibnieki/CRUD/show/all").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/kursaDalibnieki/CRUD/show/all/**").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/kursaDalibnieki/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/kursaDalibnieki/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/kursaDalibnieki/CRUD/update/**").hasAuthority("ADMIN")
				
				.requestMatchers("/kurss/CRUD/show/all").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/kurss/CRUD/show/all/**").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/kurss/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/kurss/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/kurss/CRUD/update/**").hasAuthority("ADMIN")
				
				.requestMatchers("/pasniedzeji/CRUD/show/all").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/pasniedzeji/CRUD/show/all/**").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/pasniedzeji/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/pasniedzeji/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/pasniedzeji/CRUD/update/**").hasAuthority("ADMIN")
				
				.requestMatchers("/sertifikati/CRUD/show/all").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/sertifikati/CRUD/show/all/**").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/sertifikati/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/sertifikati/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/sertifikati/CRUD/update/**").hasAuthority("ADMIN")
				);
		
		http.formLogin(auth -> auth.permitAll());
		http.csrf(auth -> auth.disable());
		
		return http.build();
	}
}
