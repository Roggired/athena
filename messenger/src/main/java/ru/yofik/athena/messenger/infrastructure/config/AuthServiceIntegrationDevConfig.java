package ru.yofik.athena.messenger.infrastructure.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.yofik.athena.messenger.infrastructure.config.properties.AuthServiceProperties;
import ru.yofik.athena.messenger.infrastructure.integration.auth.AuthUserApi;

@Configuration
@Profile("dev")
public class AuthServiceIntegrationDevConfig {
    @Bean
    public AuthUserApi authUserApi(AuthServiceProperties authServiceProperties) {
        var okhttpClient = new OkHttpClient.Builder()
                .build();
        var retrofit = new Retrofit.Builder()
                .baseUrl(authServiceProperties.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okhttpClient)
                .build();

        return retrofit.create(AuthUserApi.class);
    }
}
