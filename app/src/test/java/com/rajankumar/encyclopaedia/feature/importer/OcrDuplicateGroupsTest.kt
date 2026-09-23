package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrDuplicateGroupsTest {
  @Test fun groupsEquivalentDraftsByIndexes() {
    val drafts = listOf(
      ParsedQuestionDraft("What is CPU?", listOf("Central Processing Unit", "Memory")),
      ParsedQuestionDraft("Other question", listOf("One", "Two")),
      ParsedQuestionDraft("WHAT IS CPU", listOf("central-processing unit", "memory")),
    )
    assertEquals(listOf(0, 2), duplicateOcrDraftGroups(drafts).single().indexes)
  }
}
