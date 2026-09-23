package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreExecutionGate(
  val restoreEnabled: Boolean,
  val reviewSatisfied: Boolean
) {
  val canExecute: Boolean get() = restoreEnabled && reviewSatisfied
}

fun BackupInspection.restoreExecutionGate(warningsAcknowledged: Boolean): BackupRestoreExecutionGate {
  val gate = restoreGate()
  val acknowledgement = restoreAcknowledgement(warningsAcknowledged)
  return BackupRestoreExecutionGate(gate.canProceed, acknowledgement.canConfirm)
}
