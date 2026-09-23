package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.*

data class BackupSnapshot(
  val manifest: BackupManifest,
  val knowledgeNodes: List<KnowledgeNodeEntity>,
  val lessons: List<LessonEntity>,
  val questions: List<QuestionEntity>,
  val questionTopics: List<QuestionTopicEntity>,
  val attempts: List<QuestionAttemptEntity>,
  val plannerTasks: List<PlannerTaskEntity>,
  val notebookPages: List<NotebookPageEntity> = emptyList(),
  val notebookLayers: List<NotebookLayerEntity> = emptyList(),
  val notebookStrokes: List<NotebookStrokeEntity> = emptyList()
) {
  val recordCount: Int
    get() = knowledgeNodes.size + lessons.size + questions.size + questionTopics.size + attempts.size + plannerTasks.size + notebookPages.size + notebookLayers.size + notebookStrokes.size

  fun isInternallyConsistent(): Boolean = manifest.knowledgeNodeCount == knowledgeNodes.size && manifest.lessonCount == lessons.size && manifest.questionCount == questions.size && manifest.questionTopicCount == questionTopics.size && manifest.attemptCount == attempts.size && manifest.plannerTaskCount == plannerTasks.size && manifest.notebookPageCount == notebookPages.size && manifest.notebookLayerCount == notebookLayers.size && manifest.notebookStrokeCount == notebookStrokes.size
}
