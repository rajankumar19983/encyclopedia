package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookPageNamesTest {
  private val pages = listOf(
    NotebookPageEntity(id="one", title="Operating Systems"),
    NotebookPageEntity(id="two", title="DBMS Revision")
  )

  @Test fun whitespace_isNormalized() {
    assertEquals("Operating Systems", normalizedPageName("  Operating   Systems "))
  }

  @Test fun blankName_isRejected() {
    assertFalse(isPageNameAvailable(pages, "   "))
  }

  @Test fun duplicateName_isRejectedIgnoringCase() {
    assertFalse(isPageNameAvailable(pages, "operating systems"))
  }

  @Test fun duplicateName_isRejectedAfterWhitespaceNormalization() {
    assertFalse(isPageNameAvailable(pages, " DBMS    Revision "))
  }

  @Test fun currentPageName_isAllowedDuringRename() {
    assertTrue(isPageNameAvailable(pages, "operating systems", excludingPageId="one"))
  }

  @Test fun distinctName_isAllowed() {
    assertTrue(isPageNameAvailable(pages, "Computer Networks"))
  }

  @Test fun titleAtLimit_isAllowed() {
    assertTrue(isPageNameAvailable(pages, "P".repeat(NOTEBOOK_PAGE_TITLE_MAX_LENGTH)))
  }

  @Test fun titleOverLimit_isRejected() {
    assertFalse(isPageNameAvailable(pages, "P".repeat(NOTEBOOK_PAGE_TITLE_MAX_LENGTH + 1)))
  }
}
