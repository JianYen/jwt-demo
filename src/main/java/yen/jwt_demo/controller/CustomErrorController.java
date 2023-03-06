package yen.jwt_demo.controller;

import yen.jwt_demo.model.ResponseResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseResult handleError(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseResult(response.getStatus(), (String) request.getAttribute("javax.servlet.error.message"), null);
    }
}
