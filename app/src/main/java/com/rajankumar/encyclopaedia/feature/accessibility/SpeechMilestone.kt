package com.rajankumar.encyclopaedia.feature.accessibility

data class SpeechMilestone(
  val questionSpeech: Boolean,
  val lessonSpeech: Boolean,
  val adjustableRate: Boolean,
  val longContentChunking: Boolean,
  val accessibleControls: Boolean,
) {
  val complete: Boolean get() = questionSpeech && lessonSpeech && adjustableRate && longContentChunking && accessibleControls
}