package com.rajankumar.encyclopaedia.feature.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object TeacherConversation {
  var messages by mutableStateOf<List<TeacherMessage>>(emptyList())
    private set

  fun addStudentMessage(text: String) {
    val normalized = text.trim()
    if (normalized.isNotEmpty()) {
      messages = messages + TeacherMessage(role = TeacherMessageRole.STUDENT, text = normalized)
    }
  }

  fun addTeacherMessage(text: String) {
    val normalized = text.trim()
    if (normalized.isNotEmpty()) {
      messages = messages + TeacherMessage(role = TeacherMessageRole.TEACHER, text = normalized)
    }
  }

  fun clear() {
    messages = emptyList()
  }
}
