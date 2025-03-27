package io.gittul.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.*


@Configuration
class RestClientConfig {
    @Bean
    fun restClient(): RestClient {
        disableSSLCertificateValidation()
        return RestClient.builder().requestFactory(SimpleClientHttpRequestFactory()).build()
    }

    private fun disableSSLCertificateValidation() {
        // trust all certificates
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("TLS")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }

        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(x509Certificates: Array<X509Certificate?>?, s: String?) {
            }

            override fun checkServerTrusted(x509Certificates: Array<X509Certificate?>?, s: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })

        try {
            sslContext.init(null, trustManagers, null)
        } catch (e: KeyManagementException) {
            throw RuntimeException(e)
        }


        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)

        HttpsURLConnection.setDefaultHostnameVerifier { hostname: String?, session: SSLSession? -> true }
    }
}
