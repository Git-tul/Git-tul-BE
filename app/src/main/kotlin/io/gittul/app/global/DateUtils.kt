package io.gittul.app.global

import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDate.atEndOfDay(): LocalDateTime {
    return this.atTime(23, 59, 59)
}
