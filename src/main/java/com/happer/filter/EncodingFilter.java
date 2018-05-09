package com.happer.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter{
    private String encoder="";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoder=filterConfig.getInitParameter("encoder");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(encoder);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}
