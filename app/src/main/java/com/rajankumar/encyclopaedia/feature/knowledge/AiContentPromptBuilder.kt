package com.rajankumar.encyclopaedia.feature.knowledge

object AiContentPromptBuilder {
  fun build(request: AiContentRequest): String {
    val depthInstruction = when (request.depth) {
      AiContentDepth.QUICK -> "Create a compact outline with only the most important subtopics."
      AiContentDepth.STANDARD -> "Create a thorough exam-oriented hierarchy with important subtopics."
      AiContentDepth.DEEP -> "Create an in-depth competitive-exam hierarchy including advanced and easily confused concepts."
    }
    val lessonInstruction = if (request.includeLessons) {
      "For useful nodes, include concise but complete permanent lesson content."
    } else {
      "Do not generate lesson bodies."
    }

    return """
      Build structured study content for: ${request.topic.trim()}

      $depthInstruction
      $lessonInstruction

      Requirements:
      - Use English.
      - Optimize for competitive computer-science examinations.
      - Prefer factual, reusable study material over conversational prose.
      - Do not invent exam claims or previous-year-question provenance.
      - Return a hierarchy that can be reviewed before it is saved.
      - Keep titles concise and descriptions useful.
    """.trimIndent()
  }
}
