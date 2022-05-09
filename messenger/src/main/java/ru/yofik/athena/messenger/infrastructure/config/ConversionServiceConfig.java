package ru.yofik.athena.messenger.infrastructure.config;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConversionServiceConfig implements WebMvcConfigurer {
    @Autowired
    private ListableBeanFactory listableBeanFactory;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        var mappers = listableBeanFactory.getBeansOfType(Mapper.class).values();
        for (var mapper : mappers) {
            registry.addConverter(mapper);
        }
    }

    public interface Mapper<T, U>  extends Converter<T, U> {

    }
}
