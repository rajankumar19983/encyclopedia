package com.rajankumar.encyclopaedia.feature.knowledge

sealed interface AiProviderError {
  data object MissingApiKey : AiProviderError
  data object Unauthorized : AiProviderError
  data object RateLimited : AiProviderError
  data class Http(val statusCode: Int, val message: String? = null) : AiProviderError
  data class Network(val message: String? = null) : AiProviderError
  data class InvalidResponse(val message: String? = null) : AiProviderError
}
