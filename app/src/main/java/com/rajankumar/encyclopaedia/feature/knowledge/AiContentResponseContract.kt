package com.rajankumar.encyclopaedia.feature.knowledge

object AiContentResponseContract {
  val instruction: String = """
    Return only one JSON object using this structure:
    {
      "root": {
        "title": "Topic title",
        "description": "Optional description",
        "lessons": [
          {"title": "Lesson title", "content": "Permanent study content"}
        ],
        "children": []
      }
    }

    Rules:
    - children recursively use the same node structure.
    - lessons and children must be JSON arrays.
    - do not include markdown fences or commentary outside the JSON.
    - do not claim content is a PYQ unless provenance was explicitly supplied.
    - use English for generated study content.
  """.trimIndent()

  fun promptFor(request: AiContentRequest): String =
    AiContentPromptBuilder.build(request) + "\n\n" + instruction
}
