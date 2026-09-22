package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertTrue
import org.junit.Test

class StudyConsistencyFeedbackTest {
  @Test fun celebratesLongStreak() {
    assertTrue(studyConsistencyFeedback(StudyConsistency(12, 8, 8)).headline.contains("8-day"))
  }

  @Test fun guidesRestartAfterBreak() {
    val feedback = studyConsistencyFeedback(StudyConsistency(8, 0, 5))
    assertTrue(feedback.headline.contains("Restart"))
    assertTrue(feedback.detail.contains("5"))
  }
}
