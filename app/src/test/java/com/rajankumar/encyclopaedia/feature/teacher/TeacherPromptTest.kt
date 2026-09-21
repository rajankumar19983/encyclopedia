package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherPromptTest {
  @Test
  fun promptIncludesVisibleStudyContext() {
    val context = TeacherContext(
      screen = "Practice",
      title = "Operating Systems",
      primaryContent = "Which scheduling algorithm is preemptive?",
      supportingContent = listOf("A. Round Robin", "B. FCFS", "C. SJF")
    )

    val prompt = buildTeacherPrompt("Why is A correct?", context)

    assertTrue(prompt.contains("Current screen: Practice"))
    assertTrue(prompt.contains("Title: Operating Systems"))
    assertTrue(prompt.contains("A. Round Robin"))
    assertTrue(prompt.contains("Why is A correct?"))
  }

  @Test
  fun promptWorksWithoutScreenContext() {
    val prompt = buildTeacherPrompt("Explain deadlock prevention", null)

    assertTrue(prompt.contains("Explain deadlock prevention"))
    assertFalse(prompt.contains("VISIBLE SCREEN CONTEXT"))
  }

  @Test
  fun blankStudentQuestionIsRejected() {
    assertThrows(IllegalArgumentException::class.java) {
      buildTeacherPrompt("   ", null)
    }
  }
}
