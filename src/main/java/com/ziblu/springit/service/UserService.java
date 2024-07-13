package com.ziblu.springit.service;

import com.ziblu.springit.domain.User;
import com.ziblu.springit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
//    private final RoleService roleService;
    private final UserRepository userRepository;

//    public UserService(RoleService roleService, UserRepository userRepository)
    public UserService(UserRepository userRepository) {

//        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    public User register(User user) {
        return user;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    @Transactional
    public void saveUsers(User... users){
        for(User user: users) {
            logger.info("Saving User: " + user.getEmail());
            userRepository.save(user);
        }
    }
}