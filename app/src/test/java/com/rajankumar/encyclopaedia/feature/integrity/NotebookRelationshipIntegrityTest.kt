package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.*
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class NotebookRelationshipIntegrityTest {
  private fun snapshot(pages: List<NotebookPageEntity>, layers: List<NotebookLayerEntity>, strokes: List<NotebookStrokeEntity>) = BackupSnapshot(BackupManifest(1L, 0, 0, 0, 0, 0, 0, pages.size, layers.size, strokes.size), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), pages, layers, strokes)
  @Test fun acceptsConnectedNotebook() = assertTrue(snapshot(listOf(NotebookPageEntity("p", "Notes")), listOf(NotebookLayerEntity("l", "p", "Ink")), listOf(NotebookStrokeEntity("s", "l", "[]"))).hasValidNotebookRelationships())
  @Test fun rejectsOrphanLayer() = assertFalse(snapshot(emptyList(), listOf(NotebookLayerEntity("l", "missing", "Ink")), emptyList()).hasValidNotebookRelationships())
}
