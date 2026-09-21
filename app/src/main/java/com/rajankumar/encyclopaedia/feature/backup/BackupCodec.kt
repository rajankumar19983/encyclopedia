package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.json.JSONArray
import org.json.JSONObject

object BackupCodec {
  fun encode(snapshot: BackupSnapshot): ByteArray {
    require(snapshot.isInternallyConsistent()) { "Backup snapshot is internally inconsistent" }
    return JSONObject()
      .put("manifest", encodeManifest(snapshot.manifest))
      .put("knowledgeNodes", JSONArray(snapshot.knowledgeNodes.map(::encodeKnowledgeNode)))
      .put("lessons", JSONArray(snapshot.lessons.map(::encodeLesson)))
      .put("questions", JSONArray(snapshot.questions.map(::encodeQuestion)))
      .put("questionTopics", JSONArray(snapshot.questionTopics.map(::encodeQuestionTopic)))
      .put("attempts", JSONArray(snapshot.attempts.map(::encodeAttempt)))
      .put("plannerTasks", JSONArray(snapshot.plannerTasks.map(::encodePlannerTask)))
      .toString()
      .toByteArray(Charsets.UTF_8)
  }

  fun decode(bytes: ByteArray): BackupSnapshot {
    val root = JSONObject(String(bytes, Charsets.UTF_8))
    val manifest = decodeManifest(root.getJSONObject("manifest"))
    require(manifest.formatVersion == BACKUP_FORMAT_VERSION) {
      "Unsupported backup format version ${manifest.formatVersion}"
    }
    val snapshot = BackupSnapshot(
      manifest = manifest,
      knowledgeNodes = root.getJSONArray("knowledgeNodes").mapObjects(::decodeKnowledgeNode),
      lessons = root.getJSONArray("lessons").mapObjects(::decodeLesson),
      questions = root.getJSONArray("questions").mapObjects(::decodeQuestion),
      questionTopics = root.getJSONArray("questionTopics").mapObjects(::decodeQuestionTopic),
      attempts = root.getJSONArray("attempts").mapObjects(::decodeAttempt),
      plannerTasks = root.getJSONArray("plannerTasks").mapObjects(::decodePlannerTask)
    )
    require(snapshot.isInternallyConsistent()) { "Backup record counts do not match manifest" }
    return snapshot
  }

  private fun encodeManifest(value: BackupManifest) = JSONObject()
    .put("formatVersion", value.formatVersion)
    .put("createdAt", value.createdAt)
    .put("backupType", value.backupType.name)
    .put("knowledgeNodeCount", value.knowledgeNodeCount)
    .put("lessonCount", value.lessonCount)
    .put("questionCount", value.questionCount)
    .put("questionTopicCount", value.questionTopicCount)
    .put("attemptCount", value.attemptCount)
    .put("plannerTaskCount", value.plannerTaskCount)

  private fun decodeManifest(o: JSONObject) = BackupManifest(
    formatVersion = o.getInt("formatVersion"), createdAt = o.getLong("createdAt"),
    backupType = BackupType.valueOf(o.getString("backupType")),
    knowledgeNodeCount = o.getInt("knowledgeNodeCount"), lessonCount = o.getInt("lessonCount"),
    questionCount = o.getInt("questionCount"), questionTopicCount = o.getInt("questionTopicCount"),
    attemptCount = o.getInt("attemptCount"), plannerTaskCount = o.getInt("plannerTaskCount")
  )

  private fun encodeKnowledgeNode(v: KnowledgeNodeEntity) = JSONObject().put("id", v.id).putNullable("parentId", v.parentId).put("name", v.name).putNullable("description", v.description).put("sortOrder", v.sortOrder).put("isArchived", v.isArchived).put("createdAt", v.createdAt).put("updatedAt", v.updatedAt)
  private fun decodeKnowledgeNode(o: JSONObject) = KnowledgeNodeEntity(o.getString("id"), o.nullableString("parentId"), o.getString("name"), o.nullableString("description"), o.getInt("sortOrder"), o.getBoolean("isArchived"), o.getLong("createdAt"), o.getLong("updatedAt"))
  private fun encodeLesson(v: LessonEntity) = JSONObject().put("id", v.id).put("knowledgeNodeId", v.knowledgeNodeId).put("title", v.title).put("content", v.content).put("sortOrder", v.sortOrder).put("isArchived", v.isArchived).put("createdAt", v.createdAt).put("updatedAt", v.updatedAt)
  private fun decodeLesson(o: JSONObject) = LessonEntity(o.getString("id"), o.getString("knowledgeNodeId"), o.getString("title"), o.getString("content"), o.getInt("sortOrder"), o.getBoolean("isArchived"), o.getLong("createdAt"), o.getLong("updatedAt"))
  private fun encodeQuestion(v: QuestionEntity) = JSONObject().put("id", v.id).put("questionText", v.questionText).put("options", v.options).put("correctAnswer", v.correctAnswer).putNullable("explanation", v.explanation).put("source", v.source).put("difficulty", v.difficulty).put("createdAt", v.createdAt).put("updatedAt", v.updatedAt)
  private fun decodeQuestion(o: JSONObject) = QuestionEntity(o.getString("id"), o.getString("questionText"), o.getString("options"), o.getString("correctAnswer"), o.nullableString("explanation"), o.getString("source"), o.getString("difficulty"), o.getLong("createdAt"), o.getLong("updatedAt"))
  private fun encodeQuestionTopic(v: QuestionTopicEntity) = JSONObject().put("questionId", v.questionId).put("knowledgeNodeId", v.knowledgeNodeId)
  private fun decodeQuestionTopic(o: JSONObject) = QuestionTopicEntity(o.getString("questionId"), o.getString("knowledgeNodeId"))
  private fun encodeAttempt(v: QuestionAttemptEntity) = JSONObject().put("id", v.id).put("questionId", v.questionId).put("sessionId", v.sessionId).put("selectedAnswer", v.selectedAnswer).put("isCorrect", v.isCorrect).put("timeTakenMs", v.timeTakenMs).put("attemptedAt", v.attemptedAt)
  private fun decodeAttempt(o: JSONObject) = QuestionAttemptEntity(o.getString("id"), o.getString("questionId"), o.getString("sessionId"), o.getString("selectedAnswer"), o.getBoolean("isCorrect"), o.getLong("timeTakenMs"), o.getLong("attemptedAt"))
  private fun encodePlannerTask(v: PlannerTaskEntity) = JSONObject().put("id", v.id).put("title", v.title).put("scheduledDate", v.scheduledDate).put("isCompleted", v.isCompleted).putNullable("completedAt", v.completedAt).putNullable("carriedFromDate", v.carriedFromDate).put("createdAt", v.createdAt).put("updatedAt", v.updatedAt)
  private fun decodePlannerTask(o: JSONObject) = PlannerTaskEntity(o.getString("id"), o.getString("title"), o.getString("scheduledDate"), o.getBoolean("isCompleted"), o.nullableLong("completedAt"), o.nullableString("carriedFromDate"), o.getLong("createdAt"), o.getLong("updatedAt"))

  private fun JSONObject.putNullable(key: String, value: Any?): JSONObject = put(key, value ?: JSONObject.NULL)
  private fun JSONObject.nullableString(key: String): String? = if (isNull(key)) null else getString(key)
  private fun JSONObject.nullableLong(key: String): Long? = if (isNull(key)) null else getLong(key)
  private fun <T> JSONArray.mapObjects(transform: (JSONObject) -> T): List<T> = (0 until length()).map { transform(getJSONObject(it)) }
}
