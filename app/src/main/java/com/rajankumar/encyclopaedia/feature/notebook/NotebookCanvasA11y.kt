package com.rajankumar.encyclopaedia.feature.notebook

internal fun notebookCanvasDescription(tool: NotebookTool, strokeCount: Int, palmRejection: Boolean): String =
  "Notebook canvas. ${tool.label()} selected. $strokeCount ${if (strokeCount == 1) "stroke" else "strokes"}. Palm rejection ${if (palmRejection) "on" else "off"}."
