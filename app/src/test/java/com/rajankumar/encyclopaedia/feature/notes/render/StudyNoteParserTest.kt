package com.rajankumar.encyclopaedia.feature.notes.render

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyNoteParserTest {
  @Test
  fun parsesFrontMatterHeadingCalloutAndMermaid() {
    val document = StudyNoteParser.parse(
      """
      ---
      format-version: 1
      module: cpu
      exams: [DSSSB, BPSC]
      ---
      # Registers

      :::remember
      MAR = WHERE; MDR = WHAT.
      :::

      ```mermaid
      flowchart LR
        PC --> MAR
      ```
      """.trimIndent()
    )

    assertEquals("cpu", document.metadata.module)
    assertEquals(listOf("DSSSB", "BPSC"), document.metadata.exams)
    assertTrue(document.blocks.any { it is StudyBlock.Heading })
    assertTrue(document.blocks.any { it is StudyBlock.Callout })
    assertTrue(document.blocks.any { it is StudyBlock.Mermaid })
  }

  @Test
  fun malformedMcqFallsBackInsteadOfDisappearing() {
    val document = StudyNoteParser.parse(
      """
      :::mcq
      id: broken
      answer: 0
      :::
      """.trimIndent()
    )

    assertTrue(document.blocks.single() is StudyBlock.Unsupported)
  }

  @Test
  fun parsesInteractiveMcq() {
    val document = StudyNoteParser.parse(
      """
      :::mcq
      id: cpu-1
      question: Which register contains the current instruction?
      options:
        - MAR
        - IR
        - PC
      answer: 1
      explanation: IR holds the current instruction.
      tags: [CPU, DSSSB]
      :::
      """.trimIndent()
    )

    val mcq = document.blocks.single() as StudyBlock.Mcq
    assertEquals("IR", mcq.options[mcq.answerIndex!!])
    assertEquals(listOf("CPU", "DSSSB"), mcq.tags)
  }
}
