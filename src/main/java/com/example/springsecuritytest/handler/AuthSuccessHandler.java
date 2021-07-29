package com.example.springsecuritytest.handler;

import com.example.springsecuritytest.service.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String DEFAULT_LOGIN_SUCCESS_URL = "/home";

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        System.out.println("Spring Security Session ID : " + request.getSession().getId());
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains(Role.ADMIN.getValue())) {
            redirectStrategy.sendRedirect(request, response, "/admin" + DEFAULT_LOGIN_SUCCESS_URL);
        } else if (roles.contains(Role.MEMBER.getValue())){
            redirectStrategy.sendRedirect(request, response, "/member" + DEFAULT_LOGIN_SUCCESS_URL);
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }

    }
}
