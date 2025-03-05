package io.gittul.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class WebClientConfig {

    @Bean
    public HttpClient httpClient() throws SSLException {
        return HttpClient.create()
                .followRedirect(true) // 리다이렉션 활성화
                .secure(sslContextSpec -> {
                    try {
                        SslContext sslContext = SslContextBuilder.forClient()
                                .trustManager(InsecureTrustManager.INSTANCE)
                                .build();
                        sslContextSpec.sslContext(sslContext);
                    } catch (SSLException e) {
                        throw new RuntimeException("SSL 설정 오류", e);
                    }
                });
    }
}

/**
 * GitHub trending api 호출 시 SSL 인증서를 무시하기 위한 TrustManager
 * 크롤링으로 전환 후 자체 api 사용 예정
 */
class InsecureTrustManager implements X509TrustManager {

    static final InsecureTrustManager INSTANCE = new InsecureTrustManager();

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
