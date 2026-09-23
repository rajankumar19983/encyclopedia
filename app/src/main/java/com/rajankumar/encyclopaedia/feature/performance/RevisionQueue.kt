package com.rajankumar.encyclopaedia.feature.performance

fun buildRevisionQueue(items: List<RevisionQueueItem>): List<RevisionQueueItem> = items.sortedWith(compareByDescending<RevisionQueueItem> { it.score }.thenBy { it.title.lowercase() })
