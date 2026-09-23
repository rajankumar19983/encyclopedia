package com.rajankumar.encyclopaedia.feature.accessibility

fun speechChunks(text: String, maxLength: Int = 1200): List<String> {
  val clean = sanitizeSpeechText(text)
  if (clean.isBlank()) return emptyList()
  if (clean.length <= maxLength) return listOf(clean)
  return clean.split(Regex("(?<=[.!?])\\s+")).fold(mutableListOf()) { chunks, sentence ->
    if (chunks.isEmpty() || chunks.last().length + sentence.length + 1 > maxLength) chunks.add(sentence)
    else chunks[chunks.lastIndex] = chunks.last() + " " + sentence
    chunks
  }
}