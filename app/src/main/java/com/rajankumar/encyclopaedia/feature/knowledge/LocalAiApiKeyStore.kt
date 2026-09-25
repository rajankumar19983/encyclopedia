package com.rajankumar.encyclopaedia.feature.knowledge

import android.content.Context

/**
 * Device-local storage for a user-owned AI API key.
 *
 * The key is deliberately kept out of Room, Android backups and source code.
 * Android's private app sandbox protects this preference from other normal applications.
 */
class LocalAiApiKeyStore(context: Context) : AiApiKeySource {
  private val preferences = context.applicationContext.getSharedPreferences(
    PREFERENCES_NAME,
    Context.MODE_PRIVATE,
  )

  override fun getApiKey(): String? = preferences.getString(KEY_API_KEY, null)
    ?.trim()
    ?.takeIf(String::isNotEmpty)

  fun hasApiKey(): Boolean = getApiKey() != null

  fun saveApiKey(apiKey: String) {
    val normalized = apiKey.trim()
    require(normalized.isNotEmpty()) { "API key cannot be blank" }
    preferences.edit().putString(KEY_API_KEY, normalized).apply()
  }

  fun clearApiKey() {
    preferences.edit().remove(KEY_API_KEY).apply()
  }

  companion object {
    internal const val PREFERENCES_NAME = "local_ai_credentials"
    private const val KEY_API_KEY = "openai_api_key"
  }
}
