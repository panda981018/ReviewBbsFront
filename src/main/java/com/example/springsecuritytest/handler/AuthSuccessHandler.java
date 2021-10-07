package com.example.springsecuritytest.handler;

import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Role;
import com.example.springsecuritytest.service.CategoryService;
import com.example.springsecuritytest.service.MemberService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String DEFAULT_LOGIN_SUCCESS_URL = "/home";

    @Resource
    private MemberService memberService;
    @Resource
    private CategoryService categoryService;

    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        System.out.println("[SuccessHandler] Spring Security Session ID : " + request.getSession().getId());
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        MemberDto memberDto = memberService.findByUsername(authentication.getName());
        session.setAttribute("memberInfo", memberDto); // 회원 정보 저장

        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

        if (categoryDtoList.size() != 0) { // 카테고리가 없을 경우
            session.setAttribute("categoryList", categoryService.getAllCategories());
        }

        if (roles.contains(Role.ADMIN.getValue())) {
            redirectStrategy.sendRedirect(request, response, "/admin" + DEFAULT_LOGIN_SUCCESS_URL);
        } else if (roles.contains(Role.MEMBER.getValue())) {
            redirectStrategy.sendRedirect(request, response, "/member" + DEFAULT_LOGIN_SUCCESS_URL);
        } else {
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}
