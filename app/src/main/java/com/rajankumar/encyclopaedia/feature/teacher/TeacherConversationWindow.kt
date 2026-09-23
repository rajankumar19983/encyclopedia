package com.rajankumar.encyclopaedia.feature.teacher

fun teacherConversationWindow(messages: List<TeacherMessage>, maxMessages: Int = 12): List<TeacherMessage> {
  if (maxMessages <= 0) return emptyList()
  return messages.takeLast(maxMessages)
}
