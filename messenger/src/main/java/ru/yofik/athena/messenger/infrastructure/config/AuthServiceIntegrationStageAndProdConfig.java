package ru.yofik.athena.messenger.infrastructure.config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.yofik.athena.messenger.infrastructure.config.properties.AuthServiceProperties;
import ru.yofik.athena.messenger.infrastructure.integration.auth.AuthUserApi;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@Profile({"stage", "prod"})
public class AuthServiceIntegrationStageAndProdConfig {
    @Bean
    public AuthUserApi authUserApi(AuthServiceProperties authServiceProperties) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
//        var keyStore = KeyStore.getInstance(
//                new File(authServiceProperties.getTruststore()),
//                authServiceProperties.getTruststorePassword().toCharArray()
//        );

//        var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        trustManagerFactory.init(keyStore);
//        var trustManagers= trustManagerFactory.getTrustManagers();
//        var x509TrustManager = (X509TrustManager) trustManagers[0];

//        var sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, new X509TrustManager[] { x509TrustManager }, null);
//        var sslSocketFactory = sslContext.getSocketFactory();

        var httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        var okhttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
//                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .build();
        var retrofit = new Retrofit.Builder()
                .baseUrl(authServiceProperties.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okhttpClient)
                .build();

        return retrofit.create(AuthUserApi.class);
    }
}
