package com.rajankumar.encyclopaedia.feature.backup

fun BackupRestoreReviewSessionPresentation.warningAcknowledgementText(): String? =
  if (acknowledgementRequired) "Review every warning before enabling restore." else null
