package ru.yegorr.todolist.swagger;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.file.Path;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * Конфигурация списка
 */
@Configuration
public class SpringFoxConfig {

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
                .build().useDefaultResponseMessages(false);
    }
}
