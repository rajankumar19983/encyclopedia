package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseVersionsTest {
  @Test fun currentVersionMatchesNotebookSchema() = assertEquals(4, DatabaseVersions.CURRENT)
}
