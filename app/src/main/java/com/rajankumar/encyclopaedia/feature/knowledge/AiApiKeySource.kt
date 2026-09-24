package com.rajankumar.encyclopaedia.feature.knowledge

fun interface AiApiKeySource {
  fun getApiKey(): String?
}
