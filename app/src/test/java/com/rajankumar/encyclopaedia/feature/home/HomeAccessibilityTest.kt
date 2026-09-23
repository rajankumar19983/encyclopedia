package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeAccessibilityTest {
  @Test fun statDescriptionIncludesMeaningNotOnlyNumber() {
    val stat = HomeStatModel("Accuracy", "75%", "6 of 8 correct", 0.75f)
    assertEquals("Accuracy: 75%. 6 of 8 correct", stat.accessibilityDescription())
  }

  @Test fun recommendationReadsAsCompleteThought() {
    val recommendation = HomePlanRecommendation("Review mistakes first", "Revisit incorrect questions.")
    assertEquals("Review mistakes first. Revisit incorrect questions.", recommendation.accessibilityDescription())
  }
}
