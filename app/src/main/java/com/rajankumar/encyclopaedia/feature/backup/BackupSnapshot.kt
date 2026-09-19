package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity

data class BackupSnapshot(
  val manifest: BackupManifest,
  val knowledgeNodes: List<KnowledgeNodeEntity>,
  val lessons: List<LessonEntity>,
  val questions: List<QuestionEntity>,
  val questionTopics: List<QuestionTopicEntity>,
  val attempts: List<QuestionAttemptEntity>,
  val plannerTasks: List<PlannerTaskEntity>
) {
  fun isInternallyConsistent(): Boolean =
    manifest.knowledgeNodeCount == knowledgeNodes.size &&
      manifest.lessonCount == lessons.size &&
      manifest.questionCount == questions.size &&
      manifest.questionTopicCount == questionTopics.size &&
      manifest.attemptCount == attempts.size &&
      manifest.plannerTaskCount == plannerTasks.size
}
