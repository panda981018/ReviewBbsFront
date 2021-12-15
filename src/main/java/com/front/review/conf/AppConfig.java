package com.front.review.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    //web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //현재 localhost:9000/summernoteImg/1234.jpg
    //로 접속하면 D:/summernoteImg/1234.jpg 파일을 불러온다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/summernoteImg/**")
                .addResourceLocations("file:///C:/summernoteImg/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DTO -> Entity
    public static LocalDateTime stringToLocalDateTime(String date) {
        String str = date.replace(" ", "T");
        return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Entity -> DTO
    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.toString().replace("T", " ");
    }

    public static String getClientIp(HttpServletRequest request) {
        Logger logger = Logger.getLogger(AppConfig.class.getName());

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("> Result : IP Address : "+ip);

        return ip;
    }
}
