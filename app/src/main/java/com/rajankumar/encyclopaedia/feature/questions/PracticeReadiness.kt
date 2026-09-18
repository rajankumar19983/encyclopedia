package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class PracticeReadiness(val ready: Int, val needsReview: Int)

fun List<QuestionEntity>.practiceReadiness(): PracticeReadiness {
  val ready = count { it.assessQuality().usable }
  return PracticeReadiness(ready, size - ready)
}
