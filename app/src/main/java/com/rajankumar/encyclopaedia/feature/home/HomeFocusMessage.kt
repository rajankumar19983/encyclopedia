package com.rajankumar.encyclopaedia.feature.home

fun homeFocusMessage(revisionCount: Int, pendingTasks: Int): String = when {
  revisionCount > 0 -> "Start with revision to reinforce weak areas."
  pendingTasks > 0 -> "Continue today's planned study tasks."
  else -> "Choose a topic or practise questions to keep progressing."
}
