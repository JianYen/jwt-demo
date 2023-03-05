package yen.jwt_demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import yen.jwt_demo.entity.ResponseEntity;
import yen.jwt_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("user")
    public ResponseEntity user() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity(HttpStatus.OK.value(), "You are user", username);
    }

    @PostMapping("userLogout")
    public ResponseEntity logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.logout(username);
        return new ResponseEntity(HttpStatus.OK.value(), "成功登出", "");
    }
}