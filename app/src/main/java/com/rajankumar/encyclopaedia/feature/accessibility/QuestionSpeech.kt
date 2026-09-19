package com.rajankumar.encyclopaedia.feature.accessibility

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun QuestionEntity.asSpeakableQuestion(options: List<String>): SpeakableContent = SpeakableContent(
  title = "Question",
  body = questionText,
  supportingText = options.mapIndexed { index, option ->
    "Option ${('A'.code + index).toChar()}. $option"
  }
)

fun QuestionEntity.asSpeakableExplanation(): SpeakableContent? =
  explanation?.trim()?.takeIf(String::isNotEmpty)?.let {
    SpeakableContent(title = "Explanation", body = it)
  }
