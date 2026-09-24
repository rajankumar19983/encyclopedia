package com.rajankumar.encyclopaedia.feature.knowledge

import org.json.JSONObject

object OpenAiResponseTextExtractor {
  fun extract(raw: String): String? = runCatching {
    val root = JSONObject(raw)
    val output = root.optJSONArray("output") ?: return null
    val parts = buildList {
      for (i in 0 until output.length()) {
        val content = output.optJSONObject(i)?.optJSONArray("content") ?: continue
        for (j in 0 until content.length()) {
          val item = content.optJSONObject(j) ?: continue
          if (item.optString("type") == "output_text") {
            item.optString("text").takeIf(String::isNotBlank)?.let(::add)
          }
        }
      }
    }
    parts.takeIf { it.isNotEmpty() }?.joinToString("\n")
  }.getOrNull()
}
