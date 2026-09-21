package com.rajankumar.encyclopaedia.feature.teacher

data class OpenAiModel(
  val id: String,
  val supportsResponses: Boolean = true
)

class OpenAiModelResolver {
  fun candidates(
    available: List<OpenAiModel>,
    preference: String = OpenAiConnectionConfig.AUTOMATIC_MODEL
  ): List<String> {
    val compatible = available
      .filter(OpenAiModel::supportsResponses)
      .map(OpenAiModel::id)
      .distinct()

    if (preference != OpenAiConnectionConfig.AUTOMATIC_MODEL && preference in compatible) {
      return listOf(preference) + compatible.filterNot { it == preference }
    }

    return compatible.sortedWith(
      compareByDescending<String> { automaticPriority(it) }
        .thenByDescending { it }
    )
  }

  private fun automaticPriority(modelId: String): Int {
    val id = modelId.lowercase()
    return when {
      "mini" in id || "nano" in id -> 1
      id.startsWith("gpt-") -> 3
      else -> 2
    }
  }
}
