package com.rajankumar.encyclopaedia.feature.revision

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class RevisionItem(
  val question: QuestionEntity,
  val mistakes: Int,
  val attempts: Int,
  val priority: RevisionPriority,
  val reasons: Set<RevisionReason>
)
