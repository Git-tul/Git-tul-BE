package io.gittul.app.infra.auth.oauth.provider

enum class OauthProviderName {
    GITHUB,
    GOOGLE,
    KAKAO,
    NAVER,
    APPLE;

    companion object {
        fun fromString(value: String): OauthProviderName {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown OauthProvider: $value")
        }
    }
}
