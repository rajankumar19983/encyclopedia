package com.rajankumar.encyclopaedia.feature.teacher

data class TeacherInputValidation(val normalized: String, val valid: Boolean, val message: String? = null)

fun validateTeacherInput(input: String, maxLength: Int = 4000): TeacherInputValidation {
  val normalized = input.trim()
  return when {
    normalized.isEmpty() -> TeacherInputValidation(normalized, false, "Enter a question first.")
    normalized.length > maxLength -> TeacherInputValidation(normalized, false, "Question is too long. Keep it under $maxLength characters.")
    else -> TeacherInputValidation(normalized, true)
  }
}
