package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherConversationTest {
  @After
  fun cleanup() {
    TeacherConversation.clear()
    TeacherContextStore.clear()
  }

  @Test
  fun conversationStoresStudentAndTeacherMessages() {
    TeacherConversation.addStudentMessage("Explain paging")
    TeacherConversation.addTeacherMessage("Paging divides memory into fixed-size pages.")

    assertEquals(2, TeacherConversation.messages.size)
    assertEquals(TeacherMessageRole.STUDENT, TeacherConversation.messages[0].role)
    assertEquals(TeacherMessageRole.TEACHER, TeacherConversation.messages[1].role)
  }

  @Test
  fun blankMessagesAreIgnored() {
    TeacherConversation.addStudentMessage("   ")
    TeacherConversation.addTeacherMessage("")

    assertTrue(TeacherConversation.messages.isEmpty())
  }

  @Test
  fun requestUsesCurrentScreenContext() {
    TeacherContextStore.update(
      TeacherContext(
        screen = "Practice",
        primaryContent = "What is paging?"
      )
    )

    val request = createTeacherRequest("Explain this")

    assertTrue(request.prompt.contains("Current screen: Practice"))
    assertTrue(request.prompt.contains("What is paging?"))
    assertTrue(request.prompt.contains("Explain this"))
  }
}
