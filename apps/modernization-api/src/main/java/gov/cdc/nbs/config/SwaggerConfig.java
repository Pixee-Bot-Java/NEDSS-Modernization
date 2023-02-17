package gov.cdc.nbs.config;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build();
    }

    /**
     * Custom rule to support Pageable object in swagger ui
     * 
     * @param resolver
     * @return
     */
    @Bean
    public AlternateTypeRuleConvention pageableConvention(
            final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
                        newRule(resolver.resolve(Pageable.class),
                                resolver.resolve(pageableMixin())));
            }
        };
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .property(p -> p.name("page")
                        .type(Integer.class)
                        .canRead(true)
                        .canWrite(true))
                .property(p -> p.name("size")
                        .type(Integer.class)
                        .canRead(true)
                        .canWrite(true))
                .property(p -> p.name("sort")
                        .type(String.class)
                        .canRead(true)
                        .canWrite(true))
                .build();
    }
}