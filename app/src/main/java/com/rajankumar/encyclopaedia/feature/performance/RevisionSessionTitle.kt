package com.rajankumar.encyclopaedia.feature.performance

fun revisionSessionTitle(count: Int): String = when (count) { 0 -> "Revision queue clear"; 1 -> "1 question to revise"; else -> "$count questions to revise" }
