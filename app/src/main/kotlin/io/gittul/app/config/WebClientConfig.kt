package io.gittul.app.config

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.SslProvider.SslContextSpec
import java.security.cert.X509Certificate
import java.util.function.Consumer
import javax.net.ssl.SSLException
import javax.net.ssl.X509TrustManager

@Configuration
class WebClientConfig {
    @Bean
    @Throws(SSLException::class)
    fun httpClient(): HttpClient {
        return HttpClient.create()
            .followRedirect(true) // 리다이렉션 활성화
            .secure(Consumer { sslContextSpec: SslContextSpec? ->
                try {
                    val sslContext: SslContext = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManager.Companion.INSTANCE)
                        .build()
                    sslContextSpec!!.sslContext(sslContext)
                } catch (e: SSLException) {
                    throw RuntimeException("SSL 설정 오류", e)
                }
            })
    }
}

/**
 * GitHub trending api 호출 시 SSL 인증서를 무시하기 위한 TrustManager
 * 크롤링으로 전환 후 자체 api 사용 예정
 */
internal class InsecureTrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls<X509Certificate>(0)
    }

    companion object {
        val INSTANCE: InsecureTrustManager = InsecureTrustManager()
    }
}
