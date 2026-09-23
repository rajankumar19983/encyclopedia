package com.rajankumar.encyclopaedia.feature.teacher

data class TeacherRequest(
  val prompt: String,
  val context: TeacherContext?
)

fun createTeacherRequest(question: String): TeacherRequest {
  val context = TeacherContextStore.current
  return TeacherRequest(
    prompt = buildTeacherPrompt(question, context),
    context = context
  )
}
