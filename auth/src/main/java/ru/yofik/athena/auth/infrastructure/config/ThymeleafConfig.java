package ru.yofik.athena.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yofik.athena.common.utils.TimeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ThymeleafConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/svg/**").addResourceLocations("/svg/");
        registry.addResourceHandler("/static/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/static/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/static/img/**").addResourceLocations("/img/");
        registry.addResourceHandler("/static/fonts/**").addResourceLocations("/fonts/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateTimeToStringConverter());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        return messageSource;
    }

    public static class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
        @Override
        public String convert(LocalDateTime source) {
            if (source.equals(TimeUtils.infinity())) {
                return "никогда";
            }

            return source.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        }
    }
}
