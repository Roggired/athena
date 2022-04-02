package ru.yofik.athena.admin.infrastructure.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ThymeleafConfig
        implements WebMvcConfigurer {

//    private ApplicationContext applicationContext;
//
//
//    public void setApplicationContext(final ApplicationContext applicationContext)
//            throws BeansException {
//        this.applicationContext = applicationContext;
//    }

    /* ******************************************************************* */
    /*  GENERAL CONFIGURATION ARTIFACTS                                    */
    /*  Static Resources, i18n Messages, Formatters (Conversion Service)   */
    /* ******************************************************************* */

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/static/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/static/js/**").addResourceLocations("/js/");
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        return messageSource;
    }

//    @Override
//    public void addFormatters(final FormatterRegistry registry) {
//        super.addFormatters(registry);
//        registry.addFormatter(varietyFormatter());
//        registry.addFormatter(dateFormatter());
//    }

//    @Bean
//    public VarietyFormatter varietyFormatter() {
//        return new VarietyFormatter();
//    }

//    @Bean
//    public DateFormatter dateFormatter() {
//        return new DateFormatter();
//    }

    /* **************************************************************** */
    /*  THYMELEAF-SPECIFIC ARTIFACTS                                    */
    /*  TemplateResolver <- TemplateEngine <- ViewResolver              */
    /* **************************************************************** */

//    @Bean
//    public SpringResourceTemplateResolver templateResolver(){
//         SpringResourceTemplateResolver automatically integrates with Spring's own
//         resource resolution infrastructure, which is highly recommended.
//        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//        templateResolver.setApplicationContext(this.applicationContext);
//        templateResolver.setPrefix("/templates/");
//        templateResolver.setSuffix(".html");
//         HTML is the default value, added here for the sake of clarity.
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//         Template cache is true by default. Set to false if you want
//         templates to be automatically updated when modified.
//        templateResolver.setCacheable(true);
//        return templateResolver;
//    }

//    @Bean
//    public SpringTemplateEngine templateEngine(){
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
//        templateEngine.setEnableSpringELCompiler(true);
//        return templateEngine;
//    }

//    @Bean
//    public ThymeleafViewResolver viewResolver(){
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine());
//        return viewResolver;
//    }
}
