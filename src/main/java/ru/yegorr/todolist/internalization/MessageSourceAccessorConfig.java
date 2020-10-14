package ru.yegorr.todolist.internalization;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration of using messages in properties for validation
 */
@Configuration
public class MessageSourceAccessorConfig {

    /**
     * Creates MessageSource that uses properties
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
     * Creates LocalValidatorFactoryBean that uses messageSource
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
