package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseVersionsTest {
  @Test
  fun currentVersionMatchesKnowledgeProvenanceSchema() {
    assertEquals(5, DatabaseVersions.CURRENT)
    assertEquals(DatabaseVersions.KNOWLEDGE_PROVENANCE, DatabaseVersions.CURRENT)
  }
}
