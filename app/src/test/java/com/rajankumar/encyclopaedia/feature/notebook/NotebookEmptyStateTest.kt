package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookEmptyStateTest {
  @Test fun emptyNotebookPromptsCreation() = assertEquals("No notebook pages yet", notebookEmptyState(false).title)
  @Test fun emptySearchExplainsNoMatch() = assertEquals("No matching notes", notebookEmptyState(true).title)
}
