package com.rajankumar.encyclopaedia.feature.accessibility

fun speechChunkCountLabel(text: String): String = when (val count = speechChunks(text).size) {
  0 -> "No speech sections"
  1 -> "1 speech section"
  else -> "$count speech sections"
}
