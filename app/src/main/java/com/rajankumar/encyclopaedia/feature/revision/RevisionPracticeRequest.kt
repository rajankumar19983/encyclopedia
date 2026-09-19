package com.rajankumar.encyclopaedia.feature.revision

object RevisionPracticeRequest {
  private var questionIds: List<String> = emptyList()

  fun set(ids: List<String>) {
    questionIds = ids.distinct()
  }

  fun consume(): List<String> = questionIds.also { questionIds = emptyList() }
}
