package com.rajankumar.encyclopaedia.feature.notebook

data class NotebookUndoState(val undoCount: Int, val redoCount: Int) {
  val canUndo get() = undoCount > 0
  val canRedo get() = redoCount > 0
}
