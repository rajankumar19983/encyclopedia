package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomePlanRecommendationTest {
  @Test fun prioritisesQuestionBankWhenEmpty() = assertEquals("Build your question bank", recommendHomePlan(0, 0, 0, 0).title)
  @Test fun prioritisesBaselineBeforeHistoryExists() = assertEquals("Start a baseline session", recommendHomePlan(20, 0, 0, 0).title)
  @Test fun prioritisesMistakesForLowAccuracy() = assertEquals("Review mistakes first", recommendHomePlan(20, 10, 50, 40).title)
  @Test fun prioritisesCoverageAfterAccuracyImproves() = assertEquals("Expand your coverage", recommendHomePlan(20, 10, 50, 80).title)
  @Test fun recommendsRecallAfterFullCoverage() = assertEquals("Strengthen recall", recommendHomePlan(20, 20, 100, 80).title)
}
