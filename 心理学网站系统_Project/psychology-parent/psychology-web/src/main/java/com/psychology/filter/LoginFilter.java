package com.psychology.filter;

import com.psychology.pojo.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "loginFilter", urlPatterns = {"/toIndex"})
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("-------LoginFilter-------doFilter-----");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("user:" + user);

        if (user != null) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect("/user/toLogin");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
