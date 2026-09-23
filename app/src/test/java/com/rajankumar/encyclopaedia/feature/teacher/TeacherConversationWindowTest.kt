package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertEquals
import org.junit.Test

class TeacherConversationWindowTest {
  @Test fun keepsOnlyMostRecentMessages() {
    val messages = (1..20).map { TeacherMessage(role = TeacherMessageRole.STUDENT, text = "Message $it") }
    val window = teacherConversationWindow(messages, 5)
    assertEquals(5, window.size)
    assertEquals("Message 16", window.first().text)
    assertEquals("Message 20", window.last().text)
  }
}
