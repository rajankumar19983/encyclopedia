package com.rajankumar.encyclopaedia.data.local

internal object MigrationSql {
  val practiceTables = listOf(
    "CREATE TABLE IF NOT EXISTS `question_topics` (`questionId` TEXT NOT NULL, `knowledgeNodeId` TEXT NOT NULL, PRIMARY KEY(`questionId`, `knowledgeNodeId`), FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`knowledgeNodeId`) REFERENCES `knowledge_nodes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
    "CREATE INDEX IF NOT EXISTS `index_question_topics_questionId` ON `question_topics` (`questionId`)",
    "CREATE INDEX IF NOT EXISTS `index_question_topics_knowledgeNodeId` ON `question_topics` (`knowledgeNodeId`)",
    "CREATE TABLE IF NOT EXISTS `question_attempts` (`id` TEXT NOT NULL, `questionId` TEXT NOT NULL, `sessionId` TEXT NOT NULL, `selectedAnswer` TEXT NOT NULL, `isCorrect` INTEGER NOT NULL, `timeTakenMs` INTEGER NOT NULL, `attemptedAt` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
    "CREATE INDEX IF NOT EXISTS `index_question_attempts_questionId` ON `question_attempts` (`questionId`)",
    "CREATE INDEX IF NOT EXISTS `index_question_attempts_attemptedAt` ON `question_attempts` (`attemptedAt`)",
    "CREATE INDEX IF NOT EXISTS `index_question_attempts_sessionId` ON `question_attempts` (`sessionId`)",
  )
}
