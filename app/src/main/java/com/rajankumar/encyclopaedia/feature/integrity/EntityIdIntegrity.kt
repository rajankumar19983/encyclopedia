package com.rajankumar.encyclopaedia.feature.integrity

fun hasUniqueNonBlankIds(ids: List<String>): Boolean =
  ids.all(String::isNotBlank) && ids.size == ids.toSet().size
