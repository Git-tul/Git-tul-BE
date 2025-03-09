package io.gittul.config;

import io.gittul.config.hibernate.QueryCountInspector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String QUERY_SUMMARY_LOG_FORMAT =
            "Request Summary: Status=%d, Method=%s, URI=%s, Duration=%.3fs, Total Queries=%d, Table Stats: %s";

    private static final String TABLE_DETAIL_FORMAT = "%s=%d";

    private static final String N_PLUS_ONE_WARNING_FORMAT =
            "N+1 Warning: Potential N+1 detected! Table=%s, Queries=%d";

    private static final int N_PLUS_ONE_THRESHOLD = 5;

    private final QueryCountInspector queryCountInspector;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        queryCountInspector.startCounter();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        QueryCountInspector.Counter counter = queryCountInspector.getQueryCount();
        if (counter == null) return;

        double duration = (System.currentTimeMillis() - counter.getTime()) / 1000.0;
        long totalQueryCount = counter.getCount();
        Map<String, Long> tableCounts = counter.getTableCounts();

        // 테이블 통계 생성
        String tableStats = tableCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // 쿼리 수 기준 내림차순 정렬
                .map(entry -> {
                    String table = entry.getKey();
                    Long count = entry.getValue();
                    // N+1 경고
                    if (count >= N_PLUS_ONE_THRESHOLD) {
                        log.warn(N_PLUS_ONE_WARNING_FORMAT.formatted(table, count));
                    }
                    return TABLE_DETAIL_FORMAT.formatted(table, count);
                })
                .collect(Collectors.joining(", "));

        // 테이블 정보가 없을 경우
        if (tableStats.isEmpty()) {
            tableStats = "No table access detected";
        }

        // 요약 로그 출력
        log.info(QUERY_SUMMARY_LOG_FORMAT.formatted(
                response.getStatus(),
                request.getMethod(),
                request.getRequestURI(),
                duration,
                totalQueryCount,
                tableStats
        ));
        
        queryCountInspector.clearCounter();
    }
}
