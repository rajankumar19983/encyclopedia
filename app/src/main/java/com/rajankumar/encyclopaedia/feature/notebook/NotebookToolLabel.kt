package com.rajankumar.encyclopaedia.feature.notebook

internal fun NotebookTool.label(): String = when (this) {
  NotebookTool.PEN -> "Pen"
  NotebookTool.HIGHLIGHTER -> "Highlighter"
  NotebookTool.ERASER -> "Eraser"
  NotebookTool.LASSO -> "Lasso"
}
