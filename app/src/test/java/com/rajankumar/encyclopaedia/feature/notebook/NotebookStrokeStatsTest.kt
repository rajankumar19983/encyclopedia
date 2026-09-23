package com.rajankumar.encyclopaedia.feature.notebook

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookStrokeStatsTest {
  @Test fun countsStrokesAndPoints() {
    val strokes = listOf(CanvasStroke(listOf(Offset.Zero), 2f), CanvasStroke(listOf(Offset.Zero, Offset(1f, 1f)), 2f))
    assertEquals(NotebookStrokeStats(2, 3), notebookStrokeStats(strokes))
  }
}
