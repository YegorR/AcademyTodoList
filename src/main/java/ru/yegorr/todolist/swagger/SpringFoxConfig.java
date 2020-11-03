package ru.yegorr.todolist.swagger;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.*;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

/**
 * Конфигурация списка
 */
@Configuration
public class SpringFoxConfig {

    private static final String KEY_NAME = "JWT";

    private static ApiInfo apiInfo() {
        return new ApiInfo(
                "Todolist API",
                "Description of Todolist API",
                "0.0.1",
                "Terms of service",
                new Contact("Egor Ryazantsev", "https://github.com/YegorR", "egorryazancev1908@mail.ru"),
                "Licence",
                "https://blank.org/",
                Collections.emptyList()
        );
    }

    /**
     * Конфигирует Swagger
     *
     * @return Docket
     */
    @Bean
    public static Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any().and(PathSelectors.regex("/error").negate()))
                .build().useDefaultResponseMessages(false).securitySchemes(List.of(apiKey())).securityContexts(List.of(securityContext()));
    }

    private static ApiKey apiKey() {
        return new ApiKey(KEY_NAME, "Authorization", "header");
    }

    private static SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).operationSelector(operationContext -> true).build();
    }

    private static List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = {authorizationScope};
        return List.of(new SecurityReference(KEY_NAME, authorizationScopes));
    }
}
