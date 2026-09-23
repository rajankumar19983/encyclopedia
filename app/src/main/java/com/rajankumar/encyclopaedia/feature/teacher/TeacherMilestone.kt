package com.rajankumar.encyclopaedia.feature.teacher

data class TeacherMilestone(
  val optionalNetwork: Boolean,
  val structuredContext: Boolean,
  val answerLeakProtection: Boolean,
  val encryptedLocalKey: Boolean,
  val explicitKeyRiskNotice: Boolean,
  val failureRecovery: Boolean,
  val contextualSuggestions: Boolean,
  val privacyNotice: Boolean,
) {
  val complete: Boolean get() = optionalNetwork && structuredContext && answerLeakProtection && encryptedLocalKey && explicitKeyRiskNotice && failureRecovery && contextualSuggestions && privacyNotice
}

val currentTeacherMilestone = TeacherMilestone(true, true, true, true, true, true, true, true)
