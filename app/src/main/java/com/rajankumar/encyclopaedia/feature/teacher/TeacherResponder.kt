package com.rajankumar.encyclopaedia.feature.teacher

fun interface TeacherResponder {
  suspend fun respond(request: TeacherRequest): Result<String>
}

object TeacherResponderRegistry {
  var responder: TeacherResponder? = null
}
