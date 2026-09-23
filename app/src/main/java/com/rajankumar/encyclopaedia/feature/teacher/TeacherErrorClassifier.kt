package com.rajankumar.encyclopaedia.feature.teacher

fun classifyTeacherFailure(message: String?): TeacherConnectionState {
  val text = message.orEmpty().lowercase()
  return when {
    "401" in text || "unauthorized" in text || "api key" in text && "invalid" in text -> TeacherConnectionState.AUTH_FAILED
    "429" in text || "rate limit" in text || "quota" in text -> TeacherConnectionState.RATE_LIMITED
    "offline" in text || "unable to resolve host" in text || "network" in text || "timeout" in text -> TeacherConnectionState.OFFLINE
    else -> TeacherConnectionState.UNAVAILABLE
  }
}
