package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao

suspend fun createBackupSnapshot(
  dao: EncyclopaediaDao,
  createdAt: Long = System.currentTimeMillis()
): BackupSnapshot {
  val knowledgeNodes = dao.getAllNodesForBackup()
  val lessons = dao.getAllLessonsForBackup()
  val questions = dao.getAllQuestionsOnce()
  val questionTopics = dao.getAllQuestionTopicsForBackup()
  val attempts = dao.getAllAttemptsOnce()
  val plannerTasks = dao.getAllPlannerTasksForBackup()

  val manifest = BackupManifest(
    createdAt = createdAt,
    knowledgeNodeCount = knowledgeNodes.size,
    lessonCount = lessons.size,
    questionCount = questions.size,
    questionTopicCount = questionTopics.size,
    attemptCount = attempts.size,
    plannerTaskCount = plannerTasks.size
  )

  return BackupSnapshot(
    manifest = manifest,
    knowledgeNodes = knowledgeNodes,
    lessons = lessons,
    questions = questions,
    questionTopics = questionTopics,
    attempts = attempts,
    plannerTasks = plannerTasks
  )
}
