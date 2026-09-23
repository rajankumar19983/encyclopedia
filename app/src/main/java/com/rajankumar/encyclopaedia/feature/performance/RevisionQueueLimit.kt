package com.rajankumar.encyclopaedia.feature.performance

fun List<RevisionQueueItem>.takeRevisionSession(limit: Int): List<RevisionQueueItem> = buildRevisionQueue(this).take(limit.coerceAtLeast(0))
