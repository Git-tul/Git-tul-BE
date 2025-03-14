package io.gittul.infra.notify;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Service
public class SlackService {
    @Value("${webhook.slack.url}")
    private String SLACK_WEBHOOK_URL;

    private final Slack slackClient = Slack.getInstance();

    public void sendMessage(String title, Map<String, String> data) {

        try {
            slackClient.send(SLACK_WEBHOOK_URL, payload(p -> p
                    .text(title)
                    .attachments(List.of(
                            Attachment.builder().color(Color.GREEN.getCode())
                                    .fields(
                                            data.keySet().stream()
                                                    .map(key -> generateSlackField(key, data.get(key)))
                                                    .toList()
                                    ).build())))
            );
        } catch (IOException e) {
            log.warn("[Slack 알림] Slack 메시지 전송에 실패했습니다. title: {}, data: {}", title, data);
        }
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
