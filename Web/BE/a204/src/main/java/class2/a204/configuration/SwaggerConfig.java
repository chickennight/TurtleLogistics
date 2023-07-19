package class2.a204.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket api() {
        final ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Turtle Logistics")
                .description("<h3>RestApi 문서 제공.</h3>")
                .contact(new Contact("gitLab", "https://lab.ssafy.com/s09-webmobile3-sub2/S09P12A204", "ssafy0946119@gmail.com")).license("MIT License")
                .version("V1.0.0").build();

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("class2.a204.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}