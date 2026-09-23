package com.rajankumar.encyclopaedia.feature.accessibility

enum class SpeechQueuePolicy { REPLACE, APPEND }

fun defaultSpeechQueuePolicy(kind: SpeechContentKind): SpeechQueuePolicy = when (kind) {
  SpeechContentKind.LESSON -> SpeechQueuePolicy.APPEND
  else -> SpeechQueuePolicy.REPLACE
}