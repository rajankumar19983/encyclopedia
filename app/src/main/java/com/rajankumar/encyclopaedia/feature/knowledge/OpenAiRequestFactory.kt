package com.rajankumar.encyclopaedia.feature.knowledge

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object OpenAiRequestFactory {
  private const val RESPONSES_URL = "https://api.openai.com/v1/responses"
  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  fun create(apiKey: String, request: AiGenerationRequest): Request {
    val payload = JSONObject()
      .put("model", request.model)
      .put("input", request.prompt)
      .toString()

    return Request.Builder()
      .url(RESPONSES_URL)
      .header("Authorization", "Bearer $apiKey")
      .header("Content-Type", "application/json")
      .post(payload.toRequestBody(jsonMediaType))
      .build()
  }
}
