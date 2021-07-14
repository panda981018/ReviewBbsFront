package com.example.springsecuritytest.conf;

import com.example.springsecuritytest.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    // WebSecurity는 FilterChainProxy를 생성하는 필터.
    public void configure(WebSecurity web) throws Exception {
        // 파일 기준은 resources/static 디렉토리.
        web.ignoring().antMatchers("classpath:/resources/templates");
    }

    @Override
    // HttpStatus를 통해 HTTP 요청에 대한 웹 기반 보안을 구성할 수 있다.
    protected void configure(HttpSecurity http) throws Exception {
        // authorizeRequests() : HttpServletRequest에 따라 접근을 제한함.
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/myinfo").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
                .and()
                /* form 기반으로 인증을 하도록 함. 로그인은 기본적으로 HttpSession을 사용.
                    커스텀 로그인 form의 action 경로와 loginPage()의 파라미터경로가 일치해야 인증처리 가능.
                    .usernameParameter("파라미터명") : 이 메서드로 파라미터명을 변경할 수 있다.
                    .deleteCookies("key 명") : 로그아웃 시, 특정 쿠키를 제거하고 싶을 때 사용하는 메서드. */
                .formLogin()
                .loginPage("/user/login")
                .failureUrl("/user/login/failure")
                // 로그인이 성공했을 때 이동되는 페이지. 컨트롤러에서 URL 매핑이 되어 있어야 함.
                .defaultSuccessUrl("/user/login/result")
                .permitAll()
                .and()
                /* 로그아웃 메서드. WebSecurityConfigurerAdapter을 사용할 때 자동으로 적용됨.
                 default : "/logout"에 접근하면 Http 세션을 제거 */
                .logout()
                // 커스텀 logout url
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/logout/result")
                // Http 세션을 초기화하는 작업.
                .invalidateHttpSession(true)
                .and()
                // 예외가 발생 했을 때 핸들러를 통해서 처리할 수 있다.
                .exceptionHandling().accessDeniedPage("/user/denied");
    }

    @Override
    // 모든 인증은 AuthenticationManager를 통해 이루어지며, AuthenticationManager를 생성하기 위해서는 Builder를 사용해야함.
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
