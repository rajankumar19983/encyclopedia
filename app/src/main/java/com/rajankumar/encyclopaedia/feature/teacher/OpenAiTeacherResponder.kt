package com.rajankumar.encyclopaedia.feature.teacher

class OpenAiTeacherResponder(
  private val keyStore: OpenAiApiKeyStore,
  private val api: OpenAiApiContract,
  private val config: () -> OpenAiConnectionConfig = { OpenAiConnectionConfig() },
  private val modelResolver: OpenAiModelResolver = OpenAiModelResolver()
) : TeacherResponder {
  override suspend fun respond(request: TeacherRequest): Result<String> {
    val apiKey = keyStore.read()
      ?: return Result.failure(OpenAiNotConfiguredException())

    val settings = config()
    val available = api.listModels(apiKey).getOrElse { return Result.failure(it) }
    val candidates = modelResolver.candidates(available, settings.modelPreference)
    if (candidates.isEmpty()) {
      return Result.failure(NoCompatibleOpenAiModelException())
    }

    var lastFailure: Throwable? = null
    for (model in candidates) {
      val response = api.createResponse(
        apiKey = apiKey,
        model = model,
        prompt = request.prompt,
        responseDepth = settings.responseDepth
      )
      if (response.isSuccess) return response
      lastFailure = response.exceptionOrNull()
    }

    return Result.failure(lastFailure ?: NoCompatibleOpenAiModelException())
  }
}

class OpenAiNotConfiguredException : IllegalStateException(
  "OpenAI API key has not been configured"
)

class NoCompatibleOpenAiModelException : IllegalStateException(
  "No compatible OpenAI model is currently available"
)
