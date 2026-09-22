package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookEntityValidationTest {
  @Test fun acceptsDefaultPage() = assertTrue(NotebookPageEntity("p", "Page").hasValidNotebookFields())
  @Test fun rejectsBlankPageTitle() = assertFalse(NotebookPageEntity("p", " ").hasValidNotebookFields())
  @Test fun acceptsNormalLayer() = assertTrue(NotebookLayerEntity("l", "p", "Notes").hasValidNotebookFields())
  @Test fun rejectsBlankLayerName() = assertFalse(NotebookLayerEntity("l", "p", " ").hasValidNotebookFields())
  @Test fun acceptsNormalStroke() = assertTrue(NotebookStrokeEntity("s", "l", "[]").hasValidNotebookFields())
  @Test fun rejectsBlankStrokePoints() = assertFalse(NotebookStrokeEntity("s", "l", " ").hasValidNotebookFields())
}
