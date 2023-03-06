package yen.jwt_demo.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import yen.jwt_demo.service.UserService;
import yen.jwt_demo.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT驗證
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserDetailsService userDetailsService;
    private UserService userService;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserService userService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 判斷是否有token，並進行驗證
        Authentication token = getAuthentication(request);
        if (token == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        // 驗證成功
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String token = header.split(" ")[1];
        String username = JwtUtil.getUsername(token);
        UserDetails userDetails = null;
        if (username == null) {
            return null;
        }
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return null;
        }
        boolean isValidate = userService.verifyToken(token);
        if (!isValidate) {
            return null;
        }
//        if (!JwtUtil.verify(token, username, userDetails.getPassword())) {
//            return null;
//        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
