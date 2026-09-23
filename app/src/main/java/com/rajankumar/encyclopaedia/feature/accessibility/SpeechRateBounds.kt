package com.rajankumar.encyclopaedia.feature.accessibility

const val MIN_SPEECH_RATE = 0.5f
const val MAX_SPEECH_RATE = 2.0f

fun boundedSpeechRate(rate: Float): Float = rate.coerceIn(MIN_SPEECH_RATE, MAX_SPEECH_RATE)