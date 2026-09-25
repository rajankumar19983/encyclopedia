package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.*
import org.json.JSONArray
import org.json.JSONObject

object BackupCodec {
  fun encode(s: BackupSnapshot): ByteArray {
    require(s.isInternallyConsistent())
    return JSONObject().put("manifest", manifest(s.manifest)).put("knowledgeNodes", array(s.knowledgeNodes, ::node)).put("lessons", array(s.lessons, ::lesson)).put("questions", array(s.questions, ::question)).put("questionTopics", array(s.questionTopics, ::topic)).put("attempts", array(s.attempts, ::attempt)).put("plannerTasks", array(s.plannerTasks, ::task)).put("notebookPages", array(s.notebookPages, ::page)).put("notebookLayers", array(s.notebookLayers, ::layer)).put("notebookStrokes", array(s.notebookStrokes, ::stroke)).toString().toByteArray()
  }
  fun decode(bytes: ByteArray): BackupSnapshot {
    val r=JSONObject(String(bytes)); val m=decodeManifest(r.getJSONObject("manifest")); require(m.formatVersion in MIN_SUPPORTED_BACKUP_FORMAT_VERSION..BACKUP_FORMAT_VERSION) { "Unsupported backup format version ${m.formatVersion}" }
    val s=BackupSnapshot(m, r.getJSONArray("knowledgeNodes").map(::decodeNode), r.getJSONArray("lessons").map(::decodeLesson), r.getJSONArray("questions").map(::decodeQuestion), r.getJSONArray("questionTopics").map(::decodeTopic), r.getJSONArray("attempts").map(::decodeAttempt), r.getJSONArray("plannerTasks").map(::decodeTask), r.optJSONArray("notebookPages")?.map(::decodePage).orEmpty(), r.optJSONArray("notebookLayers")?.map(::decodeLayer).orEmpty(), r.optJSONArray("notebookStrokes")?.map(::decodeStroke).orEmpty())
    require(s.isInternallyConsistent()) { "Backup record counts do not match manifest" }; return s
  }
  private fun manifest(v: BackupManifest)=JSONObject().put("formatVersion",v.formatVersion).put("createdAt",v.createdAt).put("backupType",v.backupType.name).put("knowledgeNodeCount",v.knowledgeNodeCount).put("lessonCount",v.lessonCount).put("questionCount",v.questionCount).put("questionTopicCount",v.questionTopicCount).put("attemptCount",v.attemptCount).put("plannerTaskCount",v.plannerTaskCount).put("notebookPageCount",v.notebookPageCount).put("notebookLayerCount",v.notebookLayerCount).put("notebookStrokeCount",v.notebookStrokeCount)
  private fun decodeManifest(o:JSONObject)=BackupManifest(o.getInt("formatVersion"),o.getLong("createdAt"),BackupType.valueOf(o.getString("backupType")),o.getInt("knowledgeNodeCount"),o.getInt("lessonCount"),o.getInt("questionCount"),o.getInt("questionTopicCount"),o.getInt("attemptCount"),o.getInt("plannerTaskCount"),o.optInt("notebookPageCount",0),o.optInt("notebookLayerCount",0),o.optInt("notebookStrokeCount",0))
  private fun node(v:KnowledgeNodeEntity)=JSONObject().put("id",v.id).putN("parentId",v.parentId).put("name",v.name).putN("description",v.description).put("sortOrder",v.sortOrder).put("isArchived",v.isArchived).put("source",v.source).put("createdAt",v.createdAt).put("updatedAt",v.updatedAt)
  private fun decodeNode(o:JSONObject)=KnowledgeNodeEntity(
    id=o.getString("id"),
    parentId=o.str("parentId"),
    name=o.getString("name"),
    description=o.str("description"),
    sortOrder=o.getInt("sortOrder"),
    isArchived=o.getBoolean("isArchived"),
    source=o.contentSource(),
    createdAt=o.getLong("createdAt"),
    updatedAt=o.getLong("updatedAt")
  )
  private fun lesson(v:LessonEntity)=JSONObject().put("id",v.id).put("knowledgeNodeId",v.knowledgeNodeId).put("title",v.title).put("content",v.content).put("sortOrder",v.sortOrder).put("isArchived",v.isArchived).put("source",v.source).put("createdAt",v.createdAt).put("updatedAt",v.updatedAt)
  private fun decodeLesson(o:JSONObject)=LessonEntity(
    id=o.getString("id"),
    knowledgeNodeId=o.getString("knowledgeNodeId"),
    title=o.getString("title"),
    content=o.getString("content"),
    sortOrder=o.getInt("sortOrder"),
    isArchived=o.getBoolean("isArchived"),
    source=o.contentSource(),
    createdAt=o.getLong("createdAt"),
    updatedAt=o.getLong("updatedAt")
  )
  private fun question(v:QuestionEntity)=JSONObject().put("id",v.id).put("questionText",v.questionText).put("options",v.options).put("correctAnswer",v.correctAnswer).putN("explanation",v.explanation).put("source",v.source).put("difficulty",v.difficulty).put("createdAt",v.createdAt).put("updatedAt",v.updatedAt)
  private fun decodeQuestion(o:JSONObject)=QuestionEntity(o.getString("id"),o.getString("questionText"),o.getString("options"),o.getString("correctAnswer"),o.str("explanation"),o.getString("source"),o.getString("difficulty"),o.getLong("createdAt"),o.getLong("updatedAt"))
  private fun topic(v:QuestionTopicEntity)=JSONObject().put("questionId",v.questionId).put("knowledgeNodeId",v.knowledgeNodeId); private fun decodeTopic(o:JSONObject)=QuestionTopicEntity(o.getString("questionId"),o.getString("knowledgeNodeId"))
  private fun attempt(v:QuestionAttemptEntity)=JSONObject().put("id",v.id).put("questionId",v.questionId).put("sessionId",v.sessionId).put("selectedAnswer",v.selectedAnswer).put("isCorrect",v.isCorrect).put("timeTakenMs",v.timeTakenMs).put("attemptedAt",v.attemptedAt); private fun decodeAttempt(o:JSONObject)=QuestionAttemptEntity(o.getString("id"),o.getString("questionId"),o.getString("sessionId"),o.getString("selectedAnswer"),o.getBoolean("isCorrect"),o.getLong("timeTakenMs"),o.getLong("attemptedAt"))
  private fun task(v:PlannerTaskEntity)=JSONObject().put("id",v.id).put("title",v.title).put("scheduledDate",v.scheduledDate).put("isCompleted",v.isCompleted).putN("completedAt",v.completedAt).putN("carriedFromDate",v.carriedFromDate).put("createdAt",v.createdAt).put("updatedAt",v.updatedAt); private fun decodeTask(o:JSONObject)=PlannerTaskEntity(o.getString("id"),o.getString("title"),o.getString("scheduledDate"),o.getBoolean("isCompleted"),o.long("completedAt"),o.str("carriedFromDate"),o.getLong("createdAt"),o.getLong("updatedAt"))
  private fun page(v:NotebookPageEntity)=JSONObject().put("id",v.id).put("title",v.title).putN("knowledgeNodeId",v.knowledgeNodeId).put("pageWidth",v.pageWidth.toDouble()).put("pageHeight",v.pageHeight.toDouble()).put("background",v.background).put("sortOrder",v.sortOrder).put("createdAt",v.createdAt).put("updatedAt",v.updatedAt); private fun decodePage(o:JSONObject)=NotebookPageEntity(o.getString("id"),o.getString("title"),o.str("knowledgeNodeId"),o.getDouble("pageWidth").toFloat(),o.getDouble("pageHeight").toFloat(),o.getString("background"),o.getInt("sortOrder"),o.getLong("createdAt"),o.getLong("updatedAt"))
  private fun layer(v:NotebookLayerEntity)=JSONObject().put("id",v.id).put("pageId",v.pageId).put("name",v.name).put("sortOrder",v.sortOrder).put("isVisible",v.isVisible).put("isLocked",v.isLocked).put("createdAt",v.createdAt).put("updatedAt",v.updatedAt); private fun decodeLayer(o:JSONObject)=NotebookLayerEntity(o.getString("id"),o.getString("pageId"),o.getString("name"),o.getInt("sortOrder"),o.getBoolean("isVisible"),o.getBoolean("isLocked"),o.getLong("createdAt"),o.getLong("updatedAt"))
  private fun stroke(v:NotebookStrokeEntity)=JSONObject().put("id",v.id).put("layerId",v.layerId).put("pointsJson",v.pointsJson).put("tool",v.tool).put("colorArgb",v.colorArgb).put("width",v.width.toDouble()).put("createdAt",v.createdAt); private fun decodeStroke(o:JSONObject)=NotebookStrokeEntity(o.getString("id"),o.getString("layerId"),o.getString("pointsJson"),o.getString("tool"),o.getLong("colorArgb"),o.getDouble("width").toFloat(),o.getLong("createdAt"))
  private fun <T> array(v:List<T>,f:(T)->JSONObject)=JSONArray(v.map(f)); private fun <T> JSONArray.map(f:(JSONObject)->T)= (0 until length()).map{f(getJSONObject(it))}; private fun JSONObject.putN(k:String,v:Any?)=put(k,v?:JSONObject.NULL); private fun JSONObject.str(k:String)=if(isNull(k))null else getString(k); private fun JSONObject.long(k:String)=if(isNull(k))null else getLong(k)
  private fun JSONObject.contentSource():String=if(isNull("source")) KnowledgeContentSource.USER else optString("source",KnowledgeContentSource.USER).ifBlank { KnowledgeContentSource.USER }
}
