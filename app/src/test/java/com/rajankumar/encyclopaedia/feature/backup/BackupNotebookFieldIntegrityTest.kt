package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import org.junit.Assert.assertFalse
import org.junit.Test

class BackupNotebookFieldIntegrityTest {
  @Test
  fun rejectsInvalidNotebookPageGeometry() {
    val snapshot = emptyBackupSnapshot().copy(notebookPages = listOf(NotebookPageEntity("p", "Page", pageWidth = 1f)))
    assertFalse(snapshot.hasValidNotebookFields())
  }

  @Test
  fun rejectsUnsupportedStrokeTool() {
    val snapshot = emptyBackupSnapshot().copy(notebookStrokes = listOf(NotebookStrokeEntity("s", "l", "[]", tool = "SPRAY")))
    assertFalse(snapshot.hasValidNotebookFields())
  }
}
