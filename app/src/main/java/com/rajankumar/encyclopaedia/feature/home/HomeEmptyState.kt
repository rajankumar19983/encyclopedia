package com.rajankumar.encyclopaedia.feature.home

data class HomeEmptyState(val visible: Boolean, val title: String, val detail: String, val action: String = "")

fun homeEmptyState(topicCount: Int, questionCount: Int): HomeEmptyState = when {
  topicCount == 0 && questionCount == 0 -> HomeEmptyState(true, "Your study space is ready", "Create knowledge topics or import questions to begin building your local library.", "Import PYQs")
  questionCount == 0 -> HomeEmptyState(true, "Add practice material", "Your knowledge tree exists; add or import questions when you are ready to practise.", "Import PYQs")
  else -> HomeEmptyState(false, "", "")
}
