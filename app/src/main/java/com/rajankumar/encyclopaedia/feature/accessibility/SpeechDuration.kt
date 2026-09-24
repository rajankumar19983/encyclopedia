package com.rajankumar.encyclopaedia.feature.accessibility

fun estimatedSpeechMinutes(text: String, wordsPerMinute: Int = 160): Int {
  val words = speechWordCount(text)
  if (words == 0) return 0
  val rate = wordsPerMinute.coerceIn(60, 400)
  return ((words + rate - 1) / rate).coerceAtLeast(1)
}
