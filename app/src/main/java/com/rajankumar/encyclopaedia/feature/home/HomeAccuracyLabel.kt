package com.rajankumar.encyclopaedia.feature.home

fun homeAccuracyLabel(accuracy: Int, attempts: Int): String = if (attempts <= 0) "No accuracy data yet" else "${accuracy.coerceIn(0, 100)}% accuracy"
