package com.rajankumar.encyclopaedia.feature.teacher

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class OpenAiHttpApi(
  private val client: OkHttpClient = OkHttpClient()
) : OpenAiApiContract {
  override suspend fun listModels(apiKey: String): Result<List<OpenAiModel>> =
    execute {
      val request = Request.Builder()
        .url("$BASE_URL/models")
        .header("Authorization", "Bearer $apiKey")
        .get()
        .build()

      client.newCall(request).execute().use { response ->
        val body = response.body?.string().orEmpty()
        requireSuccessful(response.code, body)
        val data = JSONObject(body).optJSONArray("data") ?: JSONArray()
        buildList {
          for (index in 0 until data.length()) {
            val id = data.optJSONObject(index)?.optString("id").orEmpty()
            if (id.isNotBlank()) add(OpenAiModel(id, supportsResponses = isTextCandidate(id)))
          }
        }
      }
    }

  override suspend fun createResponse(
    apiKey: String,
    model: String,
    prompt: String,
    responseDepth: TeacherResponseDepth
  ): Result<String> = execute {
    val payload = JSONObject()
      .put("model", model)
      .put("input", prompt)
      .put("max_output_tokens", responseDepth.maxOutputTokens())

    val request = Request.Builder()
      .url("$BASE_URL/responses")
      .header("Authorization", "Bearer $apiKey")
      .header("Content-Type", "application/json")
      .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
      .build()

    client.newCall(request).execute().use { response ->
      val body = response.body?.string().orEmpty()
      requireSuccessful(response.code, body)
      extractOutputText(JSONObject(body))
        .takeIf(String::isNotBlank)
        ?: throw IOException("OpenAI returned no text response")
    }
  }

  private suspend fun <T> execute(block: () -> T): Result<T> = withContext(Dispatchers.IO) {
    runCatching(block)
  }

  private fun requireSuccessful(code: Int, body: String) {
    if (code in 200..299) return
    val message = runCatching {
      JSONObject(body).optJSONObject("error")?.optString("message")
    }.getOrNull().orEmpty()
    throw OpenAiHttpException(code, message.ifBlank { "OpenAI request failed" })
  }

  private fun extractOutputText(json: JSONObject): String {
    val output = json.optJSONArray("output") ?: return ""
    val pieces = mutableListOf<String>()
    for (i in 0 until output.length()) {
      val content = output.optJSONObject(i)?.optJSONArray("content") ?: continue
      for (j in 0 until content.length()) {
        val item = content.optJSONObject(j) ?: continue
        if (item.optString("type") == "output_text") {
          item.optString("text").takeIf(String::isNotBlank)?.let(pieces::add)
        }
      }
    }
    return pieces.joinToString("\n").trim()
  }

  private fun isTextCandidate(id: String): Boolean {
    val value = id.lowercase()
    return (value.startsWith("gpt-") || value.startsWith("o")) &&
      listOf("audio", "realtime", "transcribe", "tts", "image", "search").none(value::contains)
  }

  private fun TeacherResponseDepth.maxOutputTokens(): Int = when (this) {
    TeacherResponseDepth.CONCISE -> 700
    TeacherResponseDepth.DETAILED -> 1_800
    TeacherResponseDepth.VERY_DETAILED -> 3_500
  }

  companion object {
    private const val BASE_URL = "https://api.openai.com/v1"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
  }
}

class OpenAiHttpException(
  val statusCode: Int,
  override val message: String
) : IOException(message)
