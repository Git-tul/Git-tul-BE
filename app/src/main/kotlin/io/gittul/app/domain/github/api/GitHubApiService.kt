package io.gittul.app.domain.github.api

import io.gittul.app.domain.github.api.dto.RepositoryBasicInfoResponse
import io.gittul.app.domain.github.api.dto.RepositoryInfo
import io.gittul.app.global.logger
import io.gittul.core.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import java.util.function.Consumer
import java.util.function.Function

@Service
class GitHubApiService(
    webClientBuilder: WebClient.Builder
) {
    private val log = logger()

    @Value("\${github.token}")
    private val githubToken: String? = null

    // Todo. webflux 걷어내기 고려
    private val webClient: WebClient = webClientBuilder.baseUrl("https://api.github.com").build()


    fun getRepositoryInfo(owner: String, repo: String): Mono<RepositoryInfo> {
        val basicInfo = webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .header("Authorization", authorizationHeader())
            .retrieve()
            .bodyToMono<RepositoryBasicInfoResponse>(RepositoryBasicInfoResponse::class.java)
            .doOnError(Consumer { log.error("기본 정보 가져오기 실패: {}/{}", owner, repo, it) })

        val readme = webClient.get()
            .uri("/repos/{owner}/{repo}/readme", owner, repo)
            .header("Authorization", authorizationHeader())
            .header("Accept", "application/vnd.github.v3.raw")
            .retrieve()
            .bodyToMono<String>(String::class.java)
            .onErrorResume(Function {
                log.warn("README가 없음: {}/{}", owner, repo)
                Mono.just<String>("README 파일이 존재하지 않습니다.")
            })

        return Mono.zip<RepositoryBasicInfoResponse, String>(basicInfo, readme)
            .map<RepositoryInfo>(Function { tuple: Tuple2<RepositoryBasicInfoResponse, String> ->
                val basic = tuple.getT1()
                val readmeContent = tuple.getT2()
                RepositoryInfo(
                    basic,
                    readmeContent
                )
            })
            .onErrorResume(Function {
                log.error("레포지토리 정보 조합 실패: {}/{}", owner, repo, it)
                throw CustomException("레포지토리 정보를 가져오는데 실패했습니다.")
            })
    }

    private fun authorizationHeader(): String {
        return "Bearer $githubToken"
    }
}

