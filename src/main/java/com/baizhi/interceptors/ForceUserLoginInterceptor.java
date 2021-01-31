package com.baizhi.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForceUserLoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForceUserLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(request.getRequestURL().toString());
        }
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("转到登陆页:" + request.getRequestURL().toString());
            }
            response.sendRedirect("/login.jsp");
        }
        return false;
    }
}
