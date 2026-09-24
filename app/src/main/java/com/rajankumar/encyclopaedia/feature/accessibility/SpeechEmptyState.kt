package com.rajankumar.encyclopaedia.feature.accessibility

fun speechEmptyState(text: String): String? = if (sanitizeSpeechText(text).isBlank()) "There is no readable text on this screen." else null
