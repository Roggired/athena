package ru.yofik.athena.messenger.infrastructure.restApi;

import lombok.extern.log4j.Log4j2;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.*;
import java.security.cert.CertificateException;

@Component
@Log4j2
public abstract class AbstractRestTemplateApi {
    private HttpComponentsClientHttpRequestFactory requestFactory;

    @Value("${server.ssl.key-store}")
    private String keyStorePath;
    @Value("${server.ssl.key-store-type}")
    private String keyStoreType;
    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${server.ssl.trust-store}")
    private String trustStorePath;
    @Value("${server.ssl.trust-store-type}")
    private String trustStoreType;
    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;


    @PostConstruct
    public void init() {
        try {
            configureApacheHttpClient();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
            log.fatal(() -> "Can't initialize apache http client", e);
            throw new RuntimeException(e);
        }
    }

    private KeyStore getServerKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        var keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
        return keyStore;
    }

    private KeyStore getServerTrustStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        var trustStore = KeyStore.getInstance(trustStoreType);
        trustStore.load(new FileInputStream(trustStorePath), trustStorePassword.toCharArray());
        return trustStore;
    }

    private void configureApacheHttpClient() throws CertificateException,
                                                    KeyStoreException,
                                                    IOException,
                                                    NoSuchAlgorithmException,
                                                    UnrecoverableKeyException,
                                                    KeyManagementException {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(
                        new SSLContextBuilder()
                                .loadKeyMaterial(getServerKeyStore(), keyStorePassword.toCharArray())
                                .loadTrustMaterial(getServerTrustStore(), new TrustSelfSignedStrategy())
                                .build(),
                        new DefaultHostnameVerifier()
                ))
                .build();


        requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
    }

    protected ResponseEntity<String> executeRestTemplate(URI uri,
                                                    HttpMethod httpMethod,
                                                    Object requestEntity) {
        return new RestTemplate(requestFactory)
                .exchange(
                        uri,
                        httpMethod,
                        RequestEntity.post(uri).body(requestEntity),
                        String.class
                );
    }

    protected ResponseEntity<String> executeRestTemplate(URI uri,
                                                         HttpMethod httpMethod,
                                                         char[] token,
                                                         Object requestEntity) {
        return new RestTemplate(requestFactory)
                .exchange(
                        uri,
                        httpMethod,
                        RequestEntity
                                .post(uri)
                                .header("Authorization", "Bearer " + new String(token))
                                .body(requestEntity),
                        String.class
                );
    }
}
