package ru.yofik.athena.auth.infrastructure.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {
    @Bean
    public Module simpleModule() {
        var module = new SimpleModule();
        module.addDeserializer(String.class, new JsonDeserializer<>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                var stringStandardDeserialize = new StringDeserializer();
                var resultString = stringStandardDeserialize.deserialize(p, ctxt);

                if (resultString != null) {
                    return resultString.trim();
                }

                return null;
            }
        });

        return module;
    }
}
