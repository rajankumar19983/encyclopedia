package com.rajankumar.encyclopaedia.feature.integrity

fun allReferencesExist(references: Iterable<String>, availableIds: Set<String>): Boolean =
  references.all { it in availableIds }

fun allOptionalReferencesExist(references: Iterable<String?>, availableIds: Set<String>): Boolean =
  references.all { it == null || it in availableIds }
