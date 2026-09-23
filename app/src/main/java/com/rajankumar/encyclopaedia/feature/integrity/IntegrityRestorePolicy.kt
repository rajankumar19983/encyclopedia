package com.rajankumar.encyclopaedia.feature.integrity

fun IntegrityReport.canRestore(): Boolean = restoreDecision().allowed
