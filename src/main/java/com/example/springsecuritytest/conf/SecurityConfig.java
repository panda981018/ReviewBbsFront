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
                .formLogin()
                .loginPage("/user/login")
                .failureUrl("/user/login/failure")
                .defaultSuccessUrl("/user/login/result")
                .permitAll()
                .and()
                .logout()
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
