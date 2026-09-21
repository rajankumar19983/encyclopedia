package com.rajankumar.encyclopaedia.feature.teacher

/** Transport boundary kept independent from the UI so OpenAI protocol changes
 * can be handled in one place without changing the rest of Encyclopaedia. */
interface OpenAiApiContract {
  suspend fun listModels(apiKey: String): Result<List<OpenAiModel>>

  suspend fun createResponse(
    apiKey: String,
    model: String,
    prompt: String,
    responseDepth: TeacherResponseDepth
  ): Result<String>
}
