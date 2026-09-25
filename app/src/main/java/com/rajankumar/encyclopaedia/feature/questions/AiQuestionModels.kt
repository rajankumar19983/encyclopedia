package com.rajankumar.encyclopaedia.feature.questions

enum class AiQuestionDifficulty {
  MIXED,
  EASY,
  MEDIUM,
  HARD,
}

data class AiQuestionRequest(
  val topic: String,
  val count: Int = 10,
  val difficulty: AiQuestionDifficulty = AiQuestionDifficulty.MIXED,
  val referenceContext: String? = null,
  val avoidQuestionTexts: List<String> = emptyList(),
)

data class AiQuestionDraft(
  val questionText: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanation: String,
  val difficulty: String,
)

data class AiQuestionProposal(
  val questions: List<AiQuestionDraft>,
  val generatedAt: Long = System.currentTimeMillis(),
)
