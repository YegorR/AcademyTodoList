package ru.yegorr.todolist.internalization;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Конфигарция для использования сообщений из .properties для сообщениях о валидации
 */
@Configuration
public class MessageSourceAccessorConfig {

    /**
     * Создаёт MessageSource который использует .properties
     *
     * @return messageSource
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Создаёт LocalValidatorFactoryBean, который использует messageSource
     * @param messageSource messageSource
     * @return LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
