package yen.jwt_demo.security;

import yen.jwt_demo.entity.UserEntity;
import yen.jwt_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByUsername(username).get();
        if (userEntity == null) {
            throw new UsernameNotFoundException("This username didn't exist.");
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole());
    }

}
