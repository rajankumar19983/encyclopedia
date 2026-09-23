package com.rajankumar.encyclopaedia.feature.teacher

import android.content.Context

class OpenAiSettings(context: Context) {
  private val preferences = context.applicationContext.getSharedPreferences(
    PREFERENCES_NAME,
    Context.MODE_PRIVATE
  )

  fun read(): OpenAiConnectionConfig = OpenAiConnectionConfig(
    modelPreference = preferences.getString(MODEL_KEY, null)
      ?: OpenAiConnectionConfig.AUTOMATIC_MODEL,
    responseDepth = preferences.getString(DEPTH_KEY, null)
      ?.let { saved -> TeacherResponseDepth.entries.firstOrNull { it.name == saved } }
      ?: TeacherResponseDepth.DETAILED
  )

  fun save(config: OpenAiConnectionConfig) {
    preferences.edit()
      .putString(MODEL_KEY, config.modelPreference)
      .putString(DEPTH_KEY, config.responseDepth.name)
      .apply()
  }

  companion object {
    private const val PREFERENCES_NAME = "openai_settings"
    private const val MODEL_KEY = "model_preference"
    private const val DEPTH_KEY = "response_depth"
  }
}
