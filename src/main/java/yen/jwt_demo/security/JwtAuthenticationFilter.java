package yen.jwt_demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import yen.jwt_demo.entity.ResponseEntity;
import yen.jwt_demo.entity.UserEntity;
import yen.jwt_demo.service.UserService;
import yen.jwt_demo.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super();
        setAuthenticationManager(authenticationManager);
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Map<String, String> requestMap = null;
        try {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            String requestString = IOUtils.toString(request.getReader());
            requestMap = new Gson().fromJson(requestString, type);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String username = requestMap.get("username");
        String password = requestMap.get("password");
        Optional<UserEntity> userEntityOptional = userService.getUserByUsername(username);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            if (userEntity.getCanLoginTime() != null &&
                    LocalDateTime.now().compareTo(userEntity.getCanLoginTime()) < 1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "請一分鐘後再登入");
                return null;
            }
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        handleResponse(request, response, authResult, null);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        handleResponse(request, response, null, failed);
    }

    private void handleResponse(HttpServletRequest request, HttpServletResponse response, Authentication authResult, AuthenticationException failed) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity responseEntity = new ResponseEntity();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        Map<String, String> requestMap = null;
        try {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            String requestString = IOUtils.toString(request.getReader());
            requestMap = new Gson().fromJson(requestString, type);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Optional<UserEntity> userEntityOptional = userService.getUserByUsername(requestMap.get("username"));

        // 處理登入成功請求
        if (authResult != null) {
            User user = (User) authResult.getPrincipal();
            String token = JwtUtil.sign(user.getUsername(), user.getPassword());
            responseEntity.setStatus(HttpStatus.OK.value());
            responseEntity.setMsg("登入成功");
            responseEntity.setData("Bearer " + token);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                userEntity.setToken("Bearer " + token);
                userEntity.setCanLoginTime(null);
                userEntity.setAttemptLoginCount(0);
                userService.save(userEntity);
            }
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(mapper.writeValueAsString(responseEntity));
        } else {// 處理登入失敗請求
            if (userEntityOptional.isPresent()) {
                UserEntity user = userEntityOptional.get();
                user.setAttemptLoginCount(user.getAttemptLoginCount() + 1);
                UserEntity save = userService.save(user);
                //密碼錯誤次數3次會被鎖定一分鐘
                if (user.getAttemptLoginCount() >= 3) {
                    save.setCanLoginTime(LocalDateTime.now().plusMinutes(1));
                    userService.save(save);
//                    response.sendRedirect(request.getContextPath() + "/login");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "輸入錯誤次數達到上限，將鎖定一分鐘");
                    return;
                }
            }
            responseEntity.setStatus(HttpStatus.BAD_REQUEST.value());
            responseEntity.setMsg("使用者名稱或密碼錯誤");
            responseEntity.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(mapper.writeValueAsString(responseEntity));
        }
    }
}
