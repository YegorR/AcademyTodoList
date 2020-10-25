package ru.yegorr.todolist.cors;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;

/**
 * Конфигурация для генерирования AllowCorsFilter как bean и его деплоинга как фильтра
 */
@Configuration
public class AllowCorsConfig {

    /**
     * Генерирует AllowCorsFilter как бин и деплоит его как фильтр
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
