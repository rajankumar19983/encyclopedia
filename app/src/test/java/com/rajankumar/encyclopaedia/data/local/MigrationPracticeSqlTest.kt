package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertTrue
import org.junit.Test

class MigrationPracticeSqlTest {
  @Test fun practiceMigrationCreatesTablesAndIndexes() {
    val sql = MigrationSql.practiceTables.joinToString("\n")
    assertTrue(sql.contains("question_topics"))
    assertTrue(sql.contains("question_attempts"))
    assertTrue(sql.contains("sessionId"))
  }
}
