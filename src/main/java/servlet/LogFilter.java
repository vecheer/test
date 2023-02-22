package servlet;


import javax.servlet.*;
import java.io.IOException;

/**
 * @author yq
 * @version v1.0 2023-01-30 6:50 PM
 */
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("请求已经被拦截: " + servletRequest);

        filterChain.doFilter(servletRequest,servletResponse);

        System.out.println("响应已经被拦截: " + servletResponse);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("自定义 filter 正在初始化...");
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        System.out.println("自定义 filter 正在关闭...");
        Filter.super.destroy();
    }
}
