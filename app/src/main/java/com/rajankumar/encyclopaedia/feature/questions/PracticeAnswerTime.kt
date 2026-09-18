package com.rajankumar.encyclopaedia.feature.questions

fun elapsedAnswerTime(startedAt: Long, now: Long = System.currentTimeMillis()): Long =
  (now - startedAt).coerceAtLeast(0)
