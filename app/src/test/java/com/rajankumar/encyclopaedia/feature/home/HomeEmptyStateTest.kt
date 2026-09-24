package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomeEmptyStateTest {
  @Test fun emptyLibraryShowsOnboardingGuidance() = assertTrue(homeEmptyState(0, 0).visible)
  @Test fun knowledgeWithoutQuestionsPromptsPracticeMaterial() = assertTrue(homeEmptyState(4, 0).visible)
  @Test fun populatedQuestionBankHidesEmptyState() = assertFalse(homeEmptyState(4, 10).visible)
  @Test fun emptyLibraryPointsToImport() = assertEquals("Import PYQs", homeEmptyState(0, 0).action)
}
