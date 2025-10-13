package lv.sis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.MyUser;
import lv.sis.repo.IMyUserRepo;
import lv.sis.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private IMyUserRepo userRepo;
	
	@Override
	public int getUserIdFromUsername(String username) {
		MyUser user = userRepo.findByUsername(username);
		return user.getUId();
	}
}
