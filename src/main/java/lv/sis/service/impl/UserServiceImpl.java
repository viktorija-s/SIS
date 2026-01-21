package lv.sis.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.MyUser;
import lv.sis.repo.IMyUserRepo;
import lv.sis.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IMyUserRepo userRepo;

    @Override
    public int getUserIdFromUsername(String username) {
        logger.info("Fetching user ID for username: {}", username);

        MyUser user = userRepo.findByUsername(username);

        logger.debug("User found with ID: {}", user.getUId());
        return user.getUId();
    }
}

