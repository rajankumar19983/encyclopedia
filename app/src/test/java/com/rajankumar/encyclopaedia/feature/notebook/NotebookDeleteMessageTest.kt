package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookDeleteMessageTest {
  @Test fun includesPageTitle() = assertEquals("Delete DBMS? This cannot be undone.", notebookDeleteMessage(" DBMS "))
  @Test fun blankTitleUsesGenericPage() = assertEquals("Delete this page? This cannot be undone.", notebookDeleteMessage(""))
}
