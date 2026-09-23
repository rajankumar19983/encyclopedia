package com.rajankumar.encyclopaedia.data.local

internal val notebookMigrationSql = listOf(
  "CREATE TABLE IF NOT EXISTS `notebook_pages` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `knowledgeNodeId` TEXT, `pageWidth` REAL NOT NULL, `pageHeight` REAL NOT NULL, `background` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))",
  "CREATE INDEX IF NOT EXISTS `index_notebook_pages_knowledgeNodeId` ON `notebook_pages` (`knowledgeNodeId`)",
  "CREATE INDEX IF NOT EXISTS `index_notebook_pages_updatedAt` ON `notebook_pages` (`updatedAt`)",
  "CREATE TABLE IF NOT EXISTS `notebook_layers` (`id` TEXT NOT NULL, `pageId` TEXT NOT NULL, `name` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, `isVisible` INTEGER NOT NULL, `isLocked` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`pageId`) REFERENCES `notebook_pages`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
  "CREATE INDEX IF NOT EXISTS `index_notebook_layers_pageId` ON `notebook_layers` (`pageId`)",
  "CREATE TABLE IF NOT EXISTS `notebook_strokes` (`id` TEXT NOT NULL, `layerId` TEXT NOT NULL, `pointsJson` TEXT NOT NULL, `tool` TEXT NOT NULL, `colorArgb` INTEGER NOT NULL, `width` REAL NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`layerId`) REFERENCES `notebook_layers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
  "CREATE INDEX IF NOT EXISTS `index_notebook_strokes_layerId` ON `notebook_strokes` (`layerId`)",
  "CREATE INDEX IF NOT EXISTS `index_notebook_strokes_createdAt` ON `notebook_strokes` (`createdAt`)",
)
