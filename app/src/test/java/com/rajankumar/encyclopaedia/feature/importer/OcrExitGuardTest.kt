package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrExitGuardTest {
  @Test fun pendingDraftsRequireConfirmation() = assertTrue(OcrReviewSession(2, 1, 0).exitGuard().requiresConfirmation)
  @Test fun completedReviewCanExitDirectly() = assertFalse(OcrReviewSession(2, 1, 1).exitGuard().requiresConfirmation)

  @Test
  fun pendingScreenDraftsRequireDiscardConfirmation() {
    val guard = buildOcrReviewNavigationGuard(listOf(
      OcrDraftReviewState(decision = OcrReviewDecision.APPROVED),
      OcrDraftReviewState(decision = OcrReviewDecision.PENDING),
    ))

    assertFalse(guard.blocked)
    assertTrue(guard.requiresConfirmation)
    assertTrue(guard.message.orEmpty().contains("1 OCR draft"))
  }

  @Test
  fun completedScreenReviewCanNavigateDirectly() {
    val guard = buildOcrReviewNavigationGuard(listOf(
      OcrDraftReviewState(decision = OcrReviewDecision.APPROVED, saveStatus = OcrDraftSaveStatus.SAVED),
      OcrDraftReviewState(decision = OcrReviewDecision.REJECTED),
    ))

    assertFalse(guard.blocked)
    assertFalse(guard.requiresConfirmation)
    assertEquals(null, guard.message)
  }

  @Test
  fun activeSaveBlocksNavigationInsteadOfOfferingDiscard() {
    val guard = buildOcrReviewNavigationGuard(listOf(
      OcrDraftReviewState(saveStatus = OcrDraftSaveStatus.SAVING),
    ))

    assertTrue(guard.blocked)
    assertFalse(guard.requiresConfirmation)
    assertTrue(guard.message.orEmpty().contains("finish saving"))
  }

  @Test
  fun activeExtractionBlocksNavigation() {
    val guard = buildOcrReviewNavigationGuard(emptyList(), extractionInProgress = true)

    assertTrue(guard.blocked)
    assertFalse(guard.requiresConfirmation)
    assertTrue(guard.message.orEmpty().contains("OCR extraction"))
  }
}
