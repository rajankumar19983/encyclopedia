package com.rajankumar.encyclopaedia.feature.accessibility

enum class SpeechAvailability { READY, EMPTY }

fun speechAvailability(text: String): SpeechAvailability = if (sanitizeSpeechText(text).isBlank()) SpeechAvailability.EMPTY else SpeechAvailability.READY
