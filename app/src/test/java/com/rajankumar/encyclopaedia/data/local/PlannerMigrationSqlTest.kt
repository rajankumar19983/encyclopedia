package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerMigrationSqlTest {
  @Test fun plannerMigrationCreatesTableAndThreeIndexes() {
    assertEquals(4, plannerMigrationSql.size)
    assertTrue(plannerMigrationSql.first().contains("carriedFromDate"))
  }
}
