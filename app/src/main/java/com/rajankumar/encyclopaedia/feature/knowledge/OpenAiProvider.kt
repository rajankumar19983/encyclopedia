package com.rajankumar.encyclopaedia.feature.knowledge

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

class OpenAiProvider(
  private val apiKeySource: AiApiKeySource,
  private val client: OkHttpClient = OkHttpClient(),
) : AiProvider {
  override suspend fun generate(request: AiGenerationRequest): AiGenerationResult = withContext(Dispatchers.IO) {
    val apiKey = apiKeySource.getApiKey()?.trim().orEmpty()
    if (apiKey.isBlank()) return@withContext AiGenerationResult.Failure(AiProviderError.MissingApiKey)

    try {
      client.newCall(OpenAiRequestFactory.create(apiKey, request)).execute().use { response ->
        val body = response.body?.string().orEmpty()
        if (!response.isSuccessful) {
          return@withContext AiGenerationResult.Failure(mapHttpError(response.code, body))
        }
        val text = OpenAiResponseTextExtractor.extract(body)
          ?: return@withContext AiGenerationResult.Failure(AiProviderError.InvalidResponse("Response contained no output text"))
        AiGenerationResult.Success(text)
      }
    } catch (error: IOException) {
      AiGenerationResult.Failure(AiProviderError.Network(error.message))
    }
  }

  private fun mapHttpError(statusCode: Int, body: String): AiProviderError = when (statusCode) {
    401, 403 -> AiProviderError.Unauthorized
    429 -> AiProviderError.RateLimited
    else -> AiProviderError.Http(statusCode, extractErrorMessage(body))
  }

  private fun extractErrorMessage(body: String): String? = runCatching {
    org.json.JSONObject(body).optJSONObject("error")?.optString("message")?.takeIf(String::isNotBlank)
  }.getOrNull()
}
