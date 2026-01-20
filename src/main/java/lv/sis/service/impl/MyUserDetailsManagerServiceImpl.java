package lv.sis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lv.sis.config.MyUserDetails;
import lv.sis.model.MyAuthority; 
import lv.sis.model.MyUser;
import lv.sis.repo.IMyAuthorityRepo;
import lv.sis.repo.IMyUserRepo;

@Service
@NoArgsConstructor
public class MyUserDetailsManagerServiceImpl implements UserDetailsManager {
	
	@Autowired
	IMyUserRepo userRepo;
	@Autowired
	IMyAuthorityRepo authRepo;
	
	PasswordEncoder passEnc = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (userRepo.existsByUsername(username)) {
			MyUser user = userRepo.findByUsername(username);
			return new MyUserDetails(user);
		}
		else {
			throw new UsernameNotFoundException(username + " is not found");
		}
	}

	@Override
	public void createUser(UserDetails user) {
		if (user == null) {
			throw new IllegalArgumentException("User is not given");
		}
		
		String username = user.getUsername();
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username is not given");
		}

		if (userExists(username)) {
			throw new IllegalArgumentException("User " + username + " already exists");
		}
		
		MyUser newUser = new MyUser();
		
		newUser.setUsername(username);
		
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new IllegalArgumentException("Given password is not suitable");
		}
		String encodedPass = passEnc.encode(user.getPassword());
		newUser.setPassword(encodedPass);
		
		newUser.setAuthority(authRepo.findByTitle("APPLICANT"));
		
		userRepo.save(newUser);
	}

	@Override
	public void updateUser(UserDetails user) {
		if (user == null) {
			throw new IllegalArgumentException("User is not given");
		}
		
		if (user.getUsername() == null || user.getUsername().isBlank()) {
			throw new IllegalArgumentException("Username is not given");
		}
		
		MyUser userForUpdate = userRepo.findByUsername(user.getUsername());
		if (userForUpdate == null) {
			throw new UsernameNotFoundException("User " + user.getUsername() + " not found");
		}
				
		if (user.getPassword() != null || !user.getPassword().isBlank()) {
			userForUpdate.setPassword(passEnc.encode(user.getPassword()));
		}
		
		if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
			throw new IllegalArgumentException("Authority is not given");
		}
		MyAuthority auth = authRepo.findByTitle(user.getAuthorities().iterator().next().getAuthority());
		if (auth == null) {
			throw new IllegalArgumentException("Authority does not exist");
		}
		userForUpdate.setAuthority(auth);
		
		userRepo.save(userForUpdate);
	}

	@Override
	public void deleteUser(String username) {
		if (username == null || username.isBlank()) {
			throw new UsernameNotFoundException("Username is not given");
		}
		
		if (!userExists(username)) {
			throw new UsernameNotFoundException("User does not exist");
		}
		userRepo.deleteByUsername(username);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) { 
			throw new IllegalStateException("Not authenticated user found");
		}
		
		String username = auth.getName();
				
		MyUser user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " not found");
		}
		
		if (oldPassword == null || oldPassword.isBlank()) {
			throw new IllegalArgumentException("Old password is not given");
		}
		if (!passEnc.matches(oldPassword, user.getPassword())) {
			throw new IllegalArgumentException("Old password is not correct");
		}
		
		if (newPassword == null || newPassword.isBlank()) {
			throw new IllegalArgumentException("New password is not given");
		}
		
		if (passEnc.matches(newPassword, user.getPassword())) {
			throw new IllegalArgumentException("New password cannot be the same as old");
		}
		user.setPassword(passEnc.encode(newPassword));
		userRepo.save(user);
	}

	@Override
	public boolean userExists(String username) {
		return userRepo.existsByUsername(username);
	}

}
