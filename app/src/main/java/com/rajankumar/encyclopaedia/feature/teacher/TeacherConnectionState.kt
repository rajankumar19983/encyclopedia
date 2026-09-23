package com.rajankumar.encyclopaedia.feature.teacher

enum class TeacherConnectionState { NOT_CONFIGURED, READY, OFFLINE, AUTH_FAILED, RATE_LIMITED, UNAVAILABLE }

fun TeacherConnectionState.userMessage(): String = when (this) {
  TeacherConnectionState.NOT_CONFIGURED -> "Add an OpenAI API key in AI Teacher settings to use online explanations."
  TeacherConnectionState.READY -> "AI Teacher is ready."
  TeacherConnectionState.OFFLINE -> "You appear to be offline. Your study data and ordinary app features still work."
  TeacherConnectionState.AUTH_FAILED -> "The API key was rejected. Replace or rotate it in AI Teacher settings."
  TeacherConnectionState.RATE_LIMITED -> "OpenAI is temporarily rate limiting requests. Try again later."
  TeacherConnectionState.UNAVAILABLE -> "AI Teacher is temporarily unavailable. Your local study data is unaffected."
}
