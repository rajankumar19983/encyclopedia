package com.rajankumar.encyclopaedia.feature.accessibility

data class SpeechSummary(val words: Int, val sections: Int, val estimatedMinutes: Int)

fun speechSummary(text: String): SpeechSummary = SpeechSummary(speechWordCount(text), speechChunks(text).size, estimatedSpeechMinutes(text))
