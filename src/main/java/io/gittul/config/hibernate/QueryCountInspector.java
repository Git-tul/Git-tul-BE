package io.gittul.config.hibernate;

import lombok.Getter;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QueryCountInspector implements StatementInspector {

    private final ThreadLocal<Counter> queryCount = new ThreadLocal<>();
    private static final Pattern TABLE_PATTERN = Pattern.compile(
        "(?:from|join|update|into)\\s+([\\w.]+)",
        Pattern.CASE_INSENSITIVE
    );

    public void startCounter() {
        queryCount.set(new Counter(0L, System.currentTimeMillis()));
    }

    public Counter getQueryCount() {
        return queryCount.get();
    }

    public void clearCounter() {
        queryCount.remove();
    }

    @Override
    public String inspect(String sql) {
        Counter counter = queryCount.get();
        if (counter != null && isActualQuery(sql)) {
            counter.increaseCount();
            extractTableNames(sql, counter);
        }
        return sql;
    }

    private boolean isActualQuery(String sql) {
        String trimmedSql = sql.trim().toLowerCase();
        // 단순 메타데이터 쿼리 제외
        return !trimmedSql.startsWith("select @@") &&
               !trimmedSql.contains("information_schema") &&
               !trimmedSql.startsWith("show ");
    }

    private void extractTableNames(String sql, Counter counter) {
        Matcher matcher = TABLE_PATTERN.matcher(sql);
        while (matcher.find()) {
            String tableName = matcher.group(1);
            counter.incrementTableCount(tableName);
        }
    }

    @Getter
    public static class Counter {
        private Long count;
        private Long time;
        private Map<String, Long> tableCounts = new HashMap<>();

        public Counter(Long count, Long time) {
            this.count = count;
            this.time = time;
        }

        public void increaseCount() {
            count++;
        }

        public void incrementTableCount(String tableName) {
            tableCounts.put(tableName, tableCounts.getOrDefault(tableName, 0L) + 1);
        }
    }
}
