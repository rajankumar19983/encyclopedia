package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookPageHealthMessageTest {
  @Test fun blankPageInvitesWriting() = assertTrue(NotebookPageHealth.BLANK.message().contains("writing"))
  @Test fun veryDetailedPageSuggestsNewPage() = assertTrue(NotebookPageHealth.VERY_DETAILED.message().contains("new page"))
}
