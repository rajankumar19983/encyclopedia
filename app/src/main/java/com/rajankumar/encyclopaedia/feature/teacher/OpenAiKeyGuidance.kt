package com.rajankumar.encyclopaedia.feature.teacher

data class OpenAiKeyGuidance(
  val localStorageNotice: String,
  val riskNotice: String,
  val rotationNotice: String,
)

fun openAiKeyGuidance(): OpenAiKeyGuidance = OpenAiKeyGuidance(
  localStorageNotice = "Your API key is encrypted on this device, excluded from backups, and never stored in the study database.",
  riskNotice = "A key used directly by a mobile app can still be exposed on a compromised device. Use a dedicated key with limits you are comfortable with.",
  rotationNotice = "Rotate or revoke the key from your OpenAI account if you suspect exposure, then replace it here.",
)
