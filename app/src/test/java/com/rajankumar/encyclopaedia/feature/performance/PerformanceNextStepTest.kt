package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceNextStepTest {
  @Test fun noEvidenceStartsPractice() = assertEquals("Start a practice session", performanceEvidence(10,0,0,0).nextStep())
  @Test fun lowAccuracyPrioritisesMistakes() = assertEquals("Review incorrect answers", performanceEvidence(10,5,10,4).nextStep())
  @Test fun goodAccuracyExpandsCoverage() = assertEquals("Practise unseen questions", performanceEvidence(10,5,10,8).nextStep())
  @Test fun fullCoverageUsesMixedRevision() = assertEquals("Use mixed revision to strengthen recall", performanceEvidence(10,10,10,8).nextStep())
}
