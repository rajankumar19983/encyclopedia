package com.rajankumar.encyclopaedia.feature.accessibility

fun SpeechSummary.label(): String = if (words == 0) "No readable content" else "$words words • $sections ${if (sections == 1) "section" else "sections"} • about $estimatedMinutes min"
