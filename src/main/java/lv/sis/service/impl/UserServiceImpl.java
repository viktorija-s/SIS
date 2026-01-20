package lv.sis.service.impl;

import org.springframework.stereotype.Service;

import lv.sis.model.MyUser;
import lv.sis.repo.IMyUserRepo;
import lv.sis.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private IMyUserRepo userRepo;
	
	public UserServiceImpl(IMyUserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public int getUserIdFromUsername(String username) {
		MyUser user = userRepo.findByUsername(username);
		return user.getUId();
	}
}
