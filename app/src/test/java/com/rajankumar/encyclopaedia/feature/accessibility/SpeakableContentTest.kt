package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeakableContentTest {
  @Test
  fun speechTextCombinesAvailableContent() {
    val content = SpeakableContent(
      title = "Operating Systems",
      body = "A process is a program in execution",
      supportingText = listOf("Ready", "Running", "Blocked")
    )

    assertEquals(
      "Operating Systems. A process is a program in execution. Ready. Running. Blocked",
      content.asSpeechText()
    )
  }

  @Test
  fun speechTextSkipsBlankSections() {
    val content = SpeakableContent(
      title = "  ",
      body = " Virtual memory ",
      supportingText = listOf("", " Paging ", "  ")
    )

    assertEquals("Virtual memory. Paging", content.asSpeechText())
  }
}
