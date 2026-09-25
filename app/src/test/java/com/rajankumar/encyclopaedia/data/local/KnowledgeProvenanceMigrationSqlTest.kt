package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KnowledgeProvenanceMigrationSqlTest {
  @Test
  fun migrationAddsSourceToKnowledgeAndLessonsWithUserDefault() {
    assertEquals(2, knowledgeProvenanceMigrationSql.size)
    assertTrue(knowledgeProvenanceMigrationSql.any { it.contains("ALTER TABLE knowledge_nodes") })
    assertTrue(knowledgeProvenanceMigrationSql.any { it.contains("ALTER TABLE lessons") })
    assertTrue(knowledgeProvenanceMigrationSql.all { it.contains("ADD COLUMN source TEXT NOT NULL") })
    assertTrue(knowledgeProvenanceMigrationSql.all { it.contains("DEFAULT 'USER'") })
  }
}
