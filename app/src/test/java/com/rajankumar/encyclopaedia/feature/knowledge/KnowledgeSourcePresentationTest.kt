package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.data.local.KnowledgeContentSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class KnowledgeSourcePresentationTest {
  @Test
  fun `summary separates AI manual and future source values`() {
    val summary = summarizeKnowledgeSources(
      listOf(
        KnowledgeContentSource.AI,
        KnowledgeContentSource.USER,
        KnowledgeContentSource.AI,
        "IMPORTED",
      ),
    )

    assertEquals(4, summary.total)
    assertEquals(2, summary.aiGenerated)
    assertEquals(1, summary.manual)
    assertEquals(1, summary.other)
    assertEquals(2, summary.countFor(KnowledgeSourceFilter.AI_GENERATED))
  }

  @Test
  fun `only AI content receives AI generated badge`() {
    assertEquals("AI-generated", knowledgeSourceBadge(KnowledgeContentSource.AI))
    assertNull(knowledgeSourceBadge(KnowledgeContentSource.USER))
    assertNull(knowledgeSourceBadge("IMPORTED"))
  }
}
