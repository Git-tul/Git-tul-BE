package io.gittul.infra.ai.summery;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.gittul.domain.github.api.dto.RepositoryInfo;
import io.gittul.global.exception.CustomException;
import io.gittul.infra.ai.summery.dto.RepositorySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final OpenAiChatModel chatClient;
    private final ObjectMapper objectMapper;

    public RepositorySummary generateSummary(RepositoryInfo repoInfo) {
        log.debug("GitHub Repository Info: {}", repoInfo);

        String prompt = """
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
                .formatted(
                        repoInfo.basicInfo(),
                        repoInfo.readme()
                );

        try {
            String response = chatClient.call(prompt);
            log.info(response);
            return objectMapper.readValue(response, RepositorySummary.class);
        } catch (Exception e) {
            log.error("요약 생성 실패: {}", e.getMessage());
            throw new CustomException("레포지토리 요약 생성 실패: " + e.getMessage());
        }
    }
}
