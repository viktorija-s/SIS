package lv.sis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lv.sis.service.impl.MyUserDetailsManagerServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public MyUserDetailsManagerServiceImpl loadMyUserDetailsManager() {
		return new MyUserDetailsManagerServiceImpl();
	}
	
	@Bean
	public DaoAuthenticationProvider loadDaoAuthProvider() {
		PasswordEncoder passEnc = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		MyUserDetailsManagerServiceImpl service = loadMyUserDetailsManager();
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(service);
		provider.setPasswordEncoder(passEnc);
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain configureUrlsSecurity(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth 
				.requestMatchers("/home").permitAll() // main lapai
				.requestMatchers("/").permitAll()
				
				.requestMatchers("/email").hasAnyAuthority("ADMIN")
				
				.requestMatchers("/kursaDalibnieki/CRUD/show/all").hasAnyAuthority("ADMIN", "PROFESSOR")
				.requestMatchers("/kursaDalibnieki/CRUD/show/all/**").hasAnyAuthority("ADMIN", "PROFESSOR")
				.requestMatchers("/kursaDalibnieki/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/kursaDalibnieki/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/kursaDalibnieki/CRUD/update/**").hasAuthority("ADMIN")
				
				.requestMatchers("/kurss/CRUD/show/all").hasAnyAuthority("ADMIN", "PROFESSOR") 
				.requestMatchers("/kurss/CRUD/show/all/**").hasAnyAuthority("ADMIN", "PROFESSOR") 
				.requestMatchers("/kurss/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/kurss/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/kurss/CRUD/update/**").hasAnyAuthority("ADMIN") 
				
				.requestMatchers("/pasniedzeji/CRUD/show/all").hasAuthority("ADMIN")
				.requestMatchers("/pasniedzeji/CRUD/show/all/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO sees only himself
				.requestMatchers("/pasniedzeji/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/pasniedzeji/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/pasniedzeji/CRUD/update/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO can edit only himself
				
				.requestMatchers("/sertifikati/CRUD/show/all").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO can see only certificates for his courses
				.requestMatchers("/sertifikati/CRUD/show/all/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO can see only certificates for his courses
				.requestMatchers("/sertifikati/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/sertifikati/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/sertifikati/CRUD/update/**").hasAuthority("ADMIN")
				
				.requestMatchers("/vertejumi/CRUD/show/all").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses
				.requestMatchers("/vertejumi/CRUD/show/all/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses
				.requestMatchers("/vertejumi/CRUD/remove/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses
				.requestMatchers("/vertejumi/CRUD/add").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses
				.requestMatchers("/vertejumi/CRUD/update/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses

				.requestMatchers("/kursaDatumi/CRUD/show/all").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses
				.requestMatchers("/kursaDatumi/CRUD/show/all/**").hasAnyAuthority("ADMIN", "PROFESSOR") // TODO  only for his courses
				.requestMatchers("/kursaDatumi/CRUD/remove/**").hasAuthority("ADMIN")
				.requestMatchers("/kursaDatumi/CRUD/add").hasAuthority("ADMIN")
				.requestMatchers("/kursaDatumi/CRUD/update/**").hasAuthority("ADMIN")

				.requestMatchers("/pdf/**").hasAuthority("ADMIN")

				);
		
		http.formLogin(auth -> auth.permitAll());
		http.csrf(auth -> auth.disable());
		
		return http.build();
	}
}
