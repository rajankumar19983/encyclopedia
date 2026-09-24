package com.rajankumar.encyclopaedia.feature.home

import java.time.LocalTime

internal fun homeGreeting(now: LocalTime = LocalTime.now(), name: String? = null): String {
  val greeting = when (now.hour) {
    in 5..11 -> "Good morning"
    in 12..16 -> "Good afternoon"
    in 17..21 -> "Good evening"
    else -> "Welcome back"
  }
  val cleanName = name?.trim().orEmpty()
  return if (cleanName.isBlank()) greeting else "$greeting, $cleanName"
}
