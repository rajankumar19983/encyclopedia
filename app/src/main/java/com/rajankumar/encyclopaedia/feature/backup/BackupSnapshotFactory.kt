package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDao

suspend fun createBackupSnapshot(dao: EncyclopaediaDao, createdAt: Long = System.currentTimeMillis()): BackupSnapshot {
  val knowledgeNodes = dao.getAllNodesForBackup(); val lessons = dao.getAllLessonsForBackup(); val questions = dao.getAllQuestionsOnce(); val questionTopics = dao.getAllQuestionTopicsForBackup(); val attempts = dao.getAllAttemptsOnce(); val plannerTasks = dao.getAllPlannerTasksForBackup()
  val notebookPages = dao.getAllNotebookPagesForBackup(); val notebookLayers = dao.getAllNotebookLayersForBackup(); val notebookStrokes = dao.getAllNotebookStrokesForBackup()
  val manifest = BackupManifest(createdAt = createdAt, knowledgeNodeCount = knowledgeNodes.size, lessonCount = lessons.size, questionCount = questions.size, questionTopicCount = questionTopics.size, attemptCount = attempts.size, plannerTaskCount = plannerTasks.size, notebookPageCount = notebookPages.size, notebookLayerCount = notebookLayers.size, notebookStrokeCount = notebookStrokes.size)
  return BackupSnapshot(manifest, knowledgeNodes, lessons, questions, questionTopics, attempts, plannerTasks, notebookPages, notebookLayers, notebookStrokes)
}
