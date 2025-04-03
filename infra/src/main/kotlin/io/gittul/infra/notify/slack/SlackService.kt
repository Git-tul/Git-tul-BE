package io.gittul.infra.notify.slack

import com.slack.api.Slack
import com.slack.api.model.Attachment
import com.slack.api.model.Field
import com.slack.api.model.ModelConfigurator
import com.slack.api.webhook.Payload.PayloadBuilder
import com.slack.api.webhook.WebhookPayloads
import io.gittul.infra.global.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SlackService {
    @Value("\${webhook.slack.url}")
    private val SLACK_WEBHOOK_URL: String? = null

    private val slackClient: Slack = Slack.getInstance()

    fun sendMessage(title: String, data: Map<String, String>) {
        try {
            slackClient.send(
                SLACK_WEBHOOK_URL, WebhookPayloads.payload { p: PayloadBuilder? ->
                    p!!
                        .text(title)
                        .attachments(
                            listOf<Attachment>(
                                Attachment.builder().color(Color.GREEN.code)
                                    .fields(
                                        data.keys.stream()
                                            .map { generateSlackField(it, data[it]) }
                                            .toList()
                                    ).build()))
                }
            )
        } catch (e: IOException) {
            logger().warn("[Slack 알림] Slack 메시지 전송에 실패했습니다. title: {}, data: {}", title, data)
        }
    }

    private fun generateSlackField(title: String, value: String?): Field {
        return Field.builder()
            .title(title)
            .value(value)
            .valueShortEnough(false)
            .build()
    }
}
