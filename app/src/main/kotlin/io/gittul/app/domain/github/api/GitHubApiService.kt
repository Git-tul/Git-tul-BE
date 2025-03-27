package io.gittul.app.domain.github.api

import io.gittul.app.domain.github.api.dto.RepositoryBasicInfoResponse
import io.gittul.app.domain.github.api.dto.RepositoryInfo
import io.gittul.app.global.logger
import io.gittul.core.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

@Service
class GitHubApiService(
    private val restClient: RestClient
) {
    private val log = logger()

    @Value("\${github.token}")
    private val githubToken: String? = null

    fun getRepositoryInfo(owner: String, repo: String): RepositoryInfo {
        val basicInfoUrl = "https://api.github.com/repos/$owner/$repo"
        val readmeUrl = "https://api.github.com/repos/$owner/$repo/readme"

        return try {
            val headers = HttpHeaders().apply {
                add("Authorization", authorizationHeader())
            }

            val basicInfo = restClient.get()
                .uri(basicInfoUrl)
                .headers { it.addAll(headers) }
                .retrieve()
                .onStatus({ it.isError }, { _, response ->
                    throw CustomException("기본 정보 요청 실패: ${response.statusCode}")
                })
                .body(RepositoryBasicInfoResponse::class.java)
                ?: throw CustomException("기본 정보를 가져오는데 실패했습니다.")

            val readmeResponse = try {
                restClient.get()
                    .uri(readmeUrl)
                    .headers { it.addAll(headers) }
                    .retrieve()
                    .body(String::class.java)
            } catch (e: HttpClientErrorException) {
                log.warn("README 파일이 존재하지 않습니다.: {}/{}", owner, repo)
                "README 파일이 존재하지 않습니다."
            }

            println("basicInfo: $basicInfo")
            return RepositoryInfo(basicInfo, readmeResponse)
        } catch (e: Exception) {
            log.error("레포지토리 정보 조합 실패: {}/{}", owner, repo, e)
            throw CustomException("레포지토리 정보를 가져오는데 실패했습니다.")
        }
    }

    private fun authorizationHeader(): String {
        return "Bearer $githubToken"
    }
}
