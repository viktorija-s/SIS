package lv.sis.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lv.sis.model.MyUser;

public class MyUserDetails implements UserDetails {
	
	private final MyUser user;
	
	public MyUserDetails(MyUser user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority auth = new SimpleGrantedAuthority(user.getAuthority().getTitle());
		Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
		auths.add(auth);
		
		return auths;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public String getUsername() {
		return user.getUsername();
	}
}
