package io.gittul.app.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration

@EntityScan(basePackages = ["io.gittul.core.domain"])
@Configuration
class JpaConfig
