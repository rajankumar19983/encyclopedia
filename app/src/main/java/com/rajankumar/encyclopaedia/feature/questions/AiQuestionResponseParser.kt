package com.rajankumar.encyclopaedia.feature.questions

import org.json.JSONObject

sealed interface AiQuestionParseResult {
  data class Success(val proposal: AiQuestionProposal) : AiQuestionParseResult
  data class Failure(val reason: String) : AiQuestionParseResult
}

object AiQuestionResponseParser {
  fun parse(raw: String): AiQuestionParseResult {
    if (raw.isBlank()) return AiQuestionParseResult.Failure("AI response was empty")
    val start = raw.indexOf('{')
    val end = raw.lastIndexOf('}')
    if (start < 0 || end <= start) return AiQuestionParseResult.Failure("AI response did not contain a JSON object")

    return runCatching {
      val root = JSONObject(raw.substring(start, end + 1))
      val array = root.optJSONArray("questions") ?: error("Missing questions array")
      val questions = buildList {
        for (index in 0 until array.length()) {
          val item = array.getJSONObject(index)
          val optionsJson = item.optJSONArray("options") ?: error("Question ${index + 1} is missing options")
          val options = buildList {
            for (optionIndex in 0 until optionsJson.length()) add(optionsJson.getString(optionIndex).trim())
          }
          add(
            AiQuestionDraft(
              questionText = item.optString("question").trim(),
              options = options,
              correctIndex = item.optInt("correctIndex", -1),
              explanation = item.optString("explanation").trim(),
              difficulty = item.optString("difficulty", "MEDIUM").trim().uppercase(),
            ),
          )
        }
      }
      val proposal = AiQuestionProposal(questions)
      val validation = AiQuestionValidator.validate(proposal)
      if (!validation.isValid) error(validation.errors.joinToString("; "))
      AiQuestionParseResult.Success(proposal)
    }.getOrElse { error ->
      AiQuestionParseResult.Failure(error.message ?: "Could not parse AI question response")
    }
  }
}
