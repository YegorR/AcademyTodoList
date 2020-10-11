package ru.yegorr.todolist.dto.cors;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;

/**
 * Configuration for generating {@code AllowCorsFilter} as a bean and its deploying as a filter
 */
@Configuration
public class AllowCorsConfig {

    /**
     * Generate {@code AllowCorsFilter} as a bean and its deploying as a filter
     *
     * @return FilterRegistrationBean<AllowCorsFilter>
     */
    @Bean
    public FilterRegistrationBean<AllowCorsFilter> allowCorsFilterRegistrationBean() {
        FilterRegistrationBean<AllowCorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        registrationBean.setFilter(new AllowCorsFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
