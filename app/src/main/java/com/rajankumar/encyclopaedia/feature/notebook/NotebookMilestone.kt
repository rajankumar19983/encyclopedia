package com.rajankumar.encyclopaedia.feature.notebook

data class NotebookMilestone(
  val stylusInput: Boolean,
  val palmRejection: Boolean,
  val pressureWidth: Boolean,
  val layers: Boolean,
  val lasso: Boolean,
  val panZoom: Boolean,
  val persistence: Boolean,
) { val complete get() = stylusInput && palmRejection && pressureWidth && layers && lasso && panZoom && persistence }

internal val notebookCanvasMilestone = NotebookMilestone(true, true, true, true, true, true, true)
