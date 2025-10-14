package lv.sis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import lv.sis.config.MyUserDetails;
import lv.sis.model.MyUser;
import lv.sis.repo.IMyUserRepo;

@Service
public class MyUserDetailsManagerServiceImpl implements UserDetailsManager {
	
	@Autowired
	IMyUserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (userRepo.existsByUsername(username)) {
			MyUser user = userRepo.findByUsername(username);
			MyUserDetails userDetails = new MyUserDetails(user);
			return userDetails;
		}
		else {
			throw new UsernameNotFoundException(username + " is not found");
		}
	}

	@Override
	public void createUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

}
