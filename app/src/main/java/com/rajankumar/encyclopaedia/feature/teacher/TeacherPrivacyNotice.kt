package com.rajankumar.encyclopaedia.feature.teacher

fun teacherPrivacyNotice(context: TeacherContext?): String = if (context == null) {
  "Your question will be sent to OpenAI when you tap Ask. Local study features do not require this connection."
} else {
  "Your question and the structured study context shown above will be sent to OpenAI when you tap Ask. Source images are not attached."
}
