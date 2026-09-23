package com.rajankumar.encyclopaedia.data.local

internal val plannerMigrationSql = listOf(
  "CREATE TABLE IF NOT EXISTS `planner_tasks` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `scheduledDate` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `completedAt` INTEGER, `carriedFromDate` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))",
  "CREATE INDEX IF NOT EXISTS `index_planner_tasks_scheduledDate` ON `planner_tasks` (`scheduledDate`)",
  "CREATE INDEX IF NOT EXISTS `index_planner_tasks_isCompleted` ON `planner_tasks` (`isCompleted`)",
  "CREATE INDEX IF NOT EXISTS `index_planner_tasks_createdAt` ON `planner_tasks` (`createdAt`)",
)
