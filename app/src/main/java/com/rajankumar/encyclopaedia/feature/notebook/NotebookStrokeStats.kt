package com.rajankumar.encyclopaedia.feature.notebook

data class NotebookStrokeStats(val strokes: Int, val points: Int)

internal fun notebookStrokeStats(strokes: List<CanvasStroke>) = NotebookStrokeStats(strokes.size, strokes.sumOf { it.points.size })
