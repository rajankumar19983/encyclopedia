package com.rajankumar.encyclopaedia.feature.questions

enum class PracticeMode(val label: String, val description: String) {
  RANDOM("Random", "A mixed session from your question bank"),
  NEW("New questions", "Questions you have not attempted yet"),
  MISTAKES("Mistakes", "Questions you have answered incorrectly before")
}
