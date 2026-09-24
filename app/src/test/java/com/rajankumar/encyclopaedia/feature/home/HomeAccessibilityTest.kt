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
  @Test fun todayPlanReadsNextTask() = assertEquals("Today's plan. 1 of 2 tasks completed. Next task: DBMS revision.", HomeTodayPlan(1, 2, "DBMS revision").accessibilityDescription())
}
