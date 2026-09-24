package com.rajankumar.encyclopaedia.feature.knowledge

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

sealed interface AiContentParseResult {
  data class Success(val proposal: AiContentProposal) : AiContentParseResult
  data class Failure(val reason: String) : AiContentParseResult
}

object AiContentResponseParser {
  fun parse(raw: String): AiContentParseResult {
    val json = extractJson(raw) ?: return AiContentParseResult.Failure("AI response did not contain a JSON object.")
    return try {
      val rootObject = JSONObject(json)
      val root = parseNode(rootObject.optJSONObject("root") ?: rootObject)
      val proposal = AiContentProposal(root)
      val validation = AiContentValidator.validate(proposal)
      if (validation.isValid) AiContentParseResult.Success(proposal)
      else AiContentParseResult.Failure(validation.errors.joinToString())
    } catch (error: JSONException) {
      AiContentParseResult.Failure("AI response JSON is malformed: ${error.message ?: "unknown error"}")
    }
  }

  private fun parseNode(json: JSONObject): AiKnowledgeDraft {
    val lessons = json.optJSONArray("lessons").toLessons()
    val children = json.optJSONArray("children").toChildren()
    return AiKnowledgeDraft(
      title = json.optString("title").trim(),
      description = json.optString("description").trim().ifBlank { null },
      lessons = lessons,
      children = children
    )
  }

  private fun JSONArray?.toLessons(): List<AiLessonDraft> {
    if (this == null) return emptyList()
    return buildList {
      for (index in 0 until length()) {
        val lesson = optJSONObject(index) ?: continue
        add(AiLessonDraft(lesson.optString("title").trim(), lesson.optString("content").trim()))
      }
    }
  }

  private fun JSONArray?.toChildren(): List<AiKnowledgeDraft> {
    if (this == null) return emptyList()
    return buildList {
      for (index in 0 until length()) optJSONObject(index)?.let { add(parseNode(it)) }
    }
  }

  private fun extractJson(raw: String): String? {
    val trimmed = raw.trim()
    val start = trimmed.indexOf('{')
    val end = trimmed.lastIndexOf('}')
    return if (start >= 0 && end > start) trimmed.substring(start, end + 1) else null
  }
}
