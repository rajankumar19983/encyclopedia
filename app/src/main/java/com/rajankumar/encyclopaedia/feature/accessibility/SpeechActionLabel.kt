package com.rajankumar.encyclopaedia.feature.accessibility

fun speechActionLabel(kind: SpeechContentKind, speaking: Boolean): String = if (speaking) {
  "Stop reading ${kind.label().lowercase()}"
} else {
  "Read ${kind.label().lowercase()} aloud"
}