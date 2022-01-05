package com.front.review.handler;

import com.front.review.dto.MemberDto;
import com.front.review.enumclass.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Configuration
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String DEFAULT_LOGIN_SUCCESS_URL = "/home";

    @Resource
    private MemberRequestHandler loginHandler;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        log.info("[SuccessHandler] Spring Security Session ID : {}", request.getSession().getId());

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        MemberDto memberDto = loginHandler.getMemberDto(authentication.getName());
        session.setAttribute("memberInfo", memberDto);

        if (roles.contains(Role.ADMIN.getValue())) {
            redirectStrategy.sendRedirect(request, response, "/admin" + DEFAULT_LOGIN_SUCCESS_URL);
        } else if (roles.contains(Role.MEMBER.getValue())) {
            redirectStrategy.sendRedirect(request, response, "/member" + DEFAULT_LOGIN_SUCCESS_URL);
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}
