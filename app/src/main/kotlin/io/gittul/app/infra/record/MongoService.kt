package io.gittul.app.infra.record

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MongoService( //  Todo. 학습 전 임시
    private val mongoTemplate: MongoTemplate
) {
    fun <T : Any> save(entity: T): T {
        return mongoTemplate.save(entity)
    }

    fun <T : Any> findAllByDateRange(
        entityClass: Class<T>,
        startDateTime: LocalDateTime, endDateTime: LocalDateTime
    ): List<T> {
        val query = Query()
            .addCriteria(
                Criteria.where("createdAt")
                    .gte(startDateTime)
                    .lte(endDateTime)
            )
        return mongoTemplate.find(query, entityClass)
    }
}
