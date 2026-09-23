package com.rajankumar.encyclopaedia.feature.accessibility

data class SpeechProgress(val currentChunk: Int, val totalChunks: Int) {
  val fraction: Float get() = if (totalChunks <= 0) 0f else (currentChunk.toFloat() / totalChunks).coerceIn(0f, 1f)
  val label: String get() = if (totalChunks <= 0) "Not started" else "Part ${currentChunk.coerceIn(1, totalChunks)} of $totalChunks"
}