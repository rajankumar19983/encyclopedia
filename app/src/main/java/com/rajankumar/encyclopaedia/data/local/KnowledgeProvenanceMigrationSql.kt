package com.rajankumar.encyclopaedia.data.local

val knowledgeProvenanceMigrationSql = listOf(
  "ALTER TABLE knowledge_nodes ADD COLUMN source TEXT NOT NULL DEFAULT 'USER'",
  "ALTER TABLE lessons ADD COLUMN source TEXT NOT NULL DEFAULT 'USER'",
)
