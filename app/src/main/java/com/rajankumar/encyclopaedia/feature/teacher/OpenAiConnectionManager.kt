package com.rajankumar.encyclopaedia.feature.teacher

class OpenAiConnectionManager(
  private val keyStore: OpenAiApiKeyStore,
  private val api: OpenAiApiContract
) {
  fun isConfigured(): Boolean = keyStore.hasKey()

  suspend fun verify(apiKey: String): Result<Int> {
    val normalized = apiKey.trim()
    if (normalized.isEmpty()) {
      return Result.failure(IllegalArgumentException("API key cannot be blank"))
    }
    return api.listModels(normalized).map { models ->
      val compatible = models.count(OpenAiModel::supportsResponses)
      if (compatible == 0) throw NoCompatibleOpenAiModelException()
      compatible
    }
  }

  fun saveVerifiedKey(apiKey: String) = keyStore.save(apiKey)

  fun removeKey() = keyStore.remove()
}
