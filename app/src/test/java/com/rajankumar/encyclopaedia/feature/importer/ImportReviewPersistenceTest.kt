package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportReviewPersistenceTest {
  @Test
  fun savedQuestionRetainsDetectedExamAndYear() {
    val draft = ReviewDraft(
      parsed = ParsedQuestionDraft(
        questionText = "Which protocol is connection oriented?",
        options = listOf("TCP", "UDP", "IP", "ICMP"),
        correctAnswer = "A",
        explanation = "TCP establishes a connection before transferring data.",
      ),
      source = "PDF",
      metadata = OcrSourceMetadata(examName = "DSSSB", year = 2025),
    )

    val question = draft.toQuestionEntity("question-1")

    assertEquals("PDF • DSSSB • 2025", question.source)
    assertEquals("TCP\nUDP\nIP\nICMP", question.options)
    assertEquals("A", question.correctAnswer)
    assertEquals("TCP establishes a connection before transferring data.", question.explanation)
  }

  @Test
  fun savedQuestionFallsBackToInputKindWhenMetadataIsUnknown() {
    val draft = ReviewDraft(
      parsed = ParsedQuestionDraft("Choose the answer", listOf("One", "Two"), "2"),
      source = "SCAN",
      metadata = OcrSourceMetadata(),
    )

    val question = draft.toQuestionEntity("question-2")

    assertEquals("SCAN", question.source)
    assertEquals("B", question.correctAnswer)
  }

  @Test
  fun savedQuestionUsesSourceCorrectedDuringReview() {
    val draft = ReviewDraft(
      parsed = ParsedQuestionDraft("Choose the answer", listOf("One", "Two"), "A"),
      source = "PDF",
      metadata = OcrSourceMetadata(examName = "DSSSB", year = 2024),
      editableSource = "DSSSB TGT Computer Science • 2023 • Shift 2",
    )

    val question = draft.toQuestionEntity("question-3")

    assertEquals("DSSSB TGT Computer Science • 2023 • Shift 2", question.source)
  }

  @Test
  fun blankCorrectedSourceFallsBackToInputKind() {
    val draft = ReviewDraft(
      parsed = ParsedQuestionDraft("Choose the answer", listOf("One", "Two"), "A"),
      source = "PDF",
      metadata = OcrSourceMetadata(examName = "DSSSB", year = 2024),
      editableSource = "   ",
    )

    assertEquals("PDF", draft.toQuestionEntity("question-4").source)
  }
}
