package com.rajankumar.encyclopaedia.feature.home

enum class HomeQuickAction { PRACTICE, KNOWLEDGE, PLANNER, NOTEBOOK }

data class HomeQuickActionModel(val action: HomeQuickAction, val title: String, val detail: String)

fun homeQuickActions(): List<HomeQuickActionModel> = listOf(
  HomeQuickActionModel(HomeQuickAction.PRACTICE, "Practice", "Continue questions and revision"),
  HomeQuickActionModel(HomeQuickAction.KNOWLEDGE, "Knowledge", "Browse subjects, topics and lessons"),
  HomeQuickActionModel(HomeQuickAction.PLANNER, "Planner", "See today's study work"),
  HomeQuickActionModel(HomeQuickAction.NOTEBOOK, "Notebook", "Open handwritten notes")
)
