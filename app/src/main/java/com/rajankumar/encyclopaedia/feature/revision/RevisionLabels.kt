package com.rajankumar.encyclopaedia.feature.revision

fun RevisionItem.reasonLabel(): String = reasons.joinToString(" • ") { it.label }
