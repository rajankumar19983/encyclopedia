package com.rajankumar.encyclopaedia.feature.performance

fun RevisionQueueItem.accessibilityDescription(): String = "$title. ${priority.name.lowercase()} revision priority. $accuracyPercent percent accuracy after $attempts attempts. $reason."
