package yen.jwt_demo.service.impl;

import yen.jwt_demo.entity.UserEntity;
import yen.jwt_demo.repository.UserRepository;
import yen.jwt_demo.service.DataService;
import yen.jwt_demo.service.UserService;
import yen.jwt_demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private DataService dataService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean verifyToken(String token) {
        String username = JwtUtil.getUsername(token);
        UserEntity userEntity = getUserByUsername(username).get();
        if (null == userEntity.getToken()) {
            return false;
        }
        if (!userEntity.getToken().equals("Bearer " + token)) {
            return false;
        }
//        return JwtUtil.verify(token, username, userEntity.getPassword());
        return true;
    }

    @Override
    public void logout(String username) {
        UserEntity user = getUserByUsername(username).get();
        user.setToken(null);
        userRepository.save(user);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
