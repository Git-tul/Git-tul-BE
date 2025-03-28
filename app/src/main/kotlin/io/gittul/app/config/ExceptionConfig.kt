package io.gittul.app.config

import io.gittul.core.global.exception.GlobalExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(GlobalExceptionHandler::class)
@Configuration
class ExceptionConfig
