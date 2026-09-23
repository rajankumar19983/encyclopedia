package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityReport.blockingMessages(): List<String> =
  issueDetails(IntegritySeverity.ERROR).map { it.message }

fun IntegrityReport.warningMessages(): List<String> =
  issueDetails(IntegritySeverity.WARNING).map { it.message }
