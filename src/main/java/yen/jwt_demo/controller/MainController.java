package yen.jwt_demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import yen.jwt_demo.model.ResponseResult;
import yen.jwt_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    /**
     * 取得user名稱
     * @return
     */
    @GetMapping("user")
    public ResponseResult user() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseResult(HttpStatus.OK.value(), "You are user", username);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("userLogout")
    public ResponseResult logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.logout(username);
        return new ResponseResult(HttpStatus.OK.value(), "成功登出", "");
    }
}