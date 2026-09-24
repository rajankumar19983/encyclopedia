package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionAnswerFeedbackTest {
  @Test fun finalCorrectAnswerCompletesSession() = assertEquals("Correct. Revision session complete.", revisionAnswerFeedback(true, 0))
  @Test fun incorrectAnswerStaysInQueue() = assertEquals("Incorrect. Keep it in the revision queue.", revisionAnswerFeedback(false, 4))
}
