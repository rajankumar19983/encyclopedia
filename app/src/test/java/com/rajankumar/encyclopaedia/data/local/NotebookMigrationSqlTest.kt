package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookMigrationSqlTest {
  @Test fun notebookMigrationCoversPageLayerStrokeHierarchy() {
    val sql = notebookMigrationSql.joinToString("\n")
    assertTrue(sql.contains("notebook_pages"))
    assertTrue(sql.contains("notebook_layers"))
    assertTrue(sql.contains("notebook_strokes"))
    assertTrue(sql.contains("ON DELETE CASCADE"))
  }
}
