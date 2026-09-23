package com.rajankumar.encyclopaedia.feature.notebook

internal fun NotebookBackground.label(): String = when (this) {
  NotebookBackground.PLAIN -> "Plain"
  NotebookBackground.LINED -> "Lined"
  NotebookBackground.GRID -> "Grid"
}
