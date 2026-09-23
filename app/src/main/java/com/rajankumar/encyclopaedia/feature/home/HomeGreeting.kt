package com.rajankumar.encyclopaedia.feature.home

import java.time.LocalTime

internal fun homeGreeting(now: LocalTime = LocalTime.now()): String = when (now.hour) {
  in 5..11 -> "Good morning"
  in 12..16 -> "Good afternoon"
  in 17..21 -> "Good evening"
  else -> "Welcome back"
}
