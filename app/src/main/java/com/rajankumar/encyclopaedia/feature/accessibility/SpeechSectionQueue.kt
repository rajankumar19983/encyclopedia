package com.rajankumar.encyclopaedia.feature.accessibility

fun List<SpeechSection>.speechQueue(): List<String> = map { it.speechText() }.filter { it.isNotBlank() }
