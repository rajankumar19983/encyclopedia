package com.rajankumar.encyclopaedia.feature.integrity

fun hasAcyclicParents(parents: Map<String, String?>): Boolean = parents.keys.all { start ->
  val visited = mutableSetOf<String>()
  var current: String? = start
  while (current != null) {
    if (!visited.add(current)) return@all false
    current = parents[current]
  }
  true
}
