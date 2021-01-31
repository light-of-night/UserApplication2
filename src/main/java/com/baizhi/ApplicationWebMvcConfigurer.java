package com.baizhi;


import com.baizhi.interceptors.ForceUserLoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class ApplicationWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ForceUserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/formUserManager/registerUser",
                        "/formUserManager/userLogin",
                        "/kaptcha/**",
                        "/statics/**",
                        "/commons/**"
                );
    }
}
