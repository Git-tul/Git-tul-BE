package io.gittul.infra.summery

import com.fasterxml.jackson.databind.ObjectMapper
import io.gittul.infra.global.logger
import io.gittul.infra.summery.dto.RepositorySummary
import io.gittul.core.global.exception.CustomException
import io.gittul.infra.github.dto.RepositoryInfo
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.stereotype.Service


@Service
class SummaryService(
    private val chatClient: OpenAiChatModel,
    private val objectMapper: ObjectMapper
) {

    private val MAX_TOKENS = 128000 - 1000 // prompt 고려

    fun generateSummary(repoInfo: RepositoryInfo): RepositorySummary {
        logger().debug("GitHub Repository Info: {}", repoInfo)

        if (countTokens(repoInfo.basicInfo.toString()) + countTokens(repoInfo.readme) > MAX_TOKENS) {
            throw IllegalArgumentException("토큰 길이 초과로 요약 생성 불가")
        }

        val prompt: String = """
                다음 GitHub 저장소 정보를 README 기반으로 분석해 요약을 JSON 형식으로 작성해 줘.
                JSON 스키마는 다음과 같아.
                절대 전체 응답을 markdown 으로 작성하지 마
                {
                    "title": "string",
                    "description": "string",
                    "tags": ["string"]
                }
                - title: 20단어 내외, 프로젝트 핵심을 간결히.
                - description: 50단어 내외, 개요/기술/기능 포함, 가독성 높게 ( 내부는 markdown 형식 문자열)
                - tags: 5개 내외, 주요 기술/키워드/기능 포함.
                데이터: %s
                README: %s
                출력 예시:  
                {
                    "title": "Spring Boot: 빠른 Spring 앱 개발 도구", 
                    "description": "### Spring Boot는 Java로 독립 실행 앱을 쉽게 만듭니다. #### Spring 기반, 자동 설정과 내장 서버로 빠른 배포 지원. ",
                    "tags": ["Java", "Spring", "자동 설정", "배포"]
                }
                """
            .trimIndent()
            .format(
                repoInfo.basicInfo,
                repoInfo.readme
            )
        try {
            val response = chatClient.call(prompt)
            logger().debug(response)
            return objectMapper.readValue(response, RepositorySummary::class.java)
        } catch (e: Exception) {
            throw CustomException("레포지토리 요약 생성 실패: " + e.message)
        }
    }

    private fun countTokens(text: String?): Int {
        if (text == null) return 0

        val regex = Regex("\\p{L}+|\\d+|\\S")
        return regex.findAll(text).count()
    }
}
