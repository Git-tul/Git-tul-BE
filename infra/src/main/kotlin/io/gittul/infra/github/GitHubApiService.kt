package io.gittul.infra.github

import io.gittul.infra.github.dto.RepositoryBasicInfoResponse
import io.gittul.infra.github.dto.RepositoryInfo
import io.gittul.infra.global.logger
import io.gittul.core.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import java.lang.Exception
import java.lang.RuntimeException

@Service
class GitHubApiService(
    private val restClient: RestClient
) {

    @Value("\${github.token}")
    private val githubToken: String = ""

    fun getRepositoryInfo(owner: String, repo: String): RepositoryInfo {
        val basicInfo = getBasicInfo(owner, repo)
        val readmeResponse = getReadme(owner, repo)

        return RepositoryInfo(basicInfo, readmeResponse)
    }

    private fun getBasicInfo(owner: String, repo: String): RepositoryBasicInfoResponse {
        val basicInfoUrl = "https://api.github.com/repos/$owner/$repo"

        val response = restClient.get()
            .uri(basicInfoUrl)
            .headers {
                it.setBearerAuth(githubToken)
                it.accept = listOf(MediaType.APPLICATION_JSON)
            }
            .retrieve()
            .body(RepositoryBasicInfoResponse::class.java)

        return response ?: throw CustomException("레포지토리 정보 조회 중 오류 발생 : ${owner}/${repo}")
    }

    private fun getReadme(owner: String, repo: String): String {
        val readmeUrl = "https://api.github.com/repos/$owner/$repo/readme"

        val response = try {
            restClient.get()
                .uri(readmeUrl)
                .headers { it.setBearerAuth(githubToken) }
                .retrieve()
                .body(String::class.java)
        } catch (e: HttpClientErrorException) {
            logger().warn("README 파일이 존재하지 않습니다.: {}/{}", owner, repo)
            return "README 파일이 존재하지 않습니다."
        }

        return response ?: throw CustomException("README 파일을 가져오는 중 오류 발생 : ${owner}/${repo}")
    }
}
